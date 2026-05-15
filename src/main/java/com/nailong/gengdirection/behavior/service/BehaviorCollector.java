package com.nailong.gengdirection.behavior.service;

import com.nailong.gengdirection.behavior.entity.BehaviorEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import jakarta.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 行为事件收集器：内存缓冲 + 批量落盘。
 *
 * 为什么要缓冲：每来一条行为就写一次 HDFS 会产生海量小文件、且拖慢业务线程。
 * 所以先进内存队列，攒够 batchSize 条或每隔 intervalMs 统一刷到 HDFS。
 * 任何 HDFS 异常只记日志，绝不抛回业务线程（埋点失败不能影响用户正常使用）。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BehaviorCollector {

    private final HdfsService hdfsService;

    @Value("${behavior.flush.batch-size:200}")
    private int batchSize;

    private final Queue<BehaviorEvent> buffer = new ConcurrentLinkedQueue<>();
    private final AtomicLong totalCollected = new AtomicLong();
    private final AtomicLong totalFlushed = new AtomicLong();
    private volatile long lastFlushTime = 0L;
    private volatile String lastFlushPath = null;

    /** 业务侧调用：把一条行为放入缓冲（无锁、极快，不阻塞） */
    public void collect(BehaviorEvent event) {
        if (event == null) {
            return;
        }
        buffer.offer(event);
        totalCollected.incrementAndGet();
        if (buffer.size() >= batchSize) {
            flush();
        }
    }

    /** 定时批量落盘，间隔由 behavior.flush.interval-ms 配置 */
    @Scheduled(fixedDelayString = "${behavior.flush.interval-ms:30000}")
    public synchronized void flush() {
        if (buffer.isEmpty()) {
            return;
        }
        List<BehaviorEvent> batch = new ArrayList<>();
        BehaviorEvent e;
        while ((e = buffer.poll()) != null) {
            batch.add(e);
        }
        try {
            String path = hdfsService.writeBatch(batch);
            totalFlushed.addAndGet(batch.size());
            lastFlushTime = System.currentTimeMillis();
            lastFlushPath = path;
        } catch (Exception ex) {
            // 落盘失败：把数据放回队列下次重试（HDFS 恢复后不丢数据），仅记日志
            buffer.addAll(batch);
            log.warn("[采集] 批量写 HDFS 失败，{} 条事件已放回队列等待重试: {}",
                    batch.size(), ex.getMessage());
        }
    }

    public int bufferedCount() {
        return buffer.size();
    }

    public long totalCollected() {
        return totalCollected.get();
    }

    public long totalFlushed() {
        return totalFlushed.get();
    }

    public long lastFlushTime() {
        return lastFlushTime;
    }

    public String lastFlushPath() {
        return lastFlushPath;
    }

    @PreDestroy
    public void onShutdown() {
        log.info("[采集] 应用关闭，最后一次落盘 {} 条", buffer.size());
        flush();
    }
}
