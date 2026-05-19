package com.nailong.gengdirection.behavior.controller;

import com.nailong.gengdirection.behavior.dto.TrackDTO;
import com.nailong.gengdirection.behavior.entity.ActionType;
import com.nailong.gengdirection.behavior.entity.BehaviorEvent;
import com.nailong.gengdirection.behavior.service.BehaviorCollector;
import com.nailong.gengdirection.behavior.service.HdfsService;
import com.nailong.gengdirection.common.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 行为采集 REST 接口（数据采集层入口）。
 *
 * - POST /behavior/track  前端主动上报一条行为
 * - POST /behavior/flush  立即把内存缓冲刷入 HDFS（演示/截图用）
 * - GET  /behavior/status 查看采集状态 + HDFS 连通性
 */
@RestController
@RequestMapping("/behavior")
@RequiredArgsConstructor
public class BehaviorController {

    private final BehaviorCollector collector;
    private final HdfsService hdfsService;

    @PostMapping("/track")
    public Result<Void> track(@RequestBody TrackDTO dto) {
        collector.collect(BehaviorEvent.of(
                dto.getUserId(),
                ActionType.safeOf(dto.getAction()),
                dto.getPostId()));
        return Result.success();
    }

    @PostMapping("/flush")
    public Result<Map<String, Object>> flush() {
        int before = collector.bufferedCount();
        collector.flush();
        Map<String, Object> r = new LinkedHashMap<>();
        r.put("flushedRequested", before);
        r.put("bufferedAfter", collector.bufferedCount());
        r.put("lastFlushPath", collector.lastFlushPath());
        return Result.success(r);
    }

    @GetMapping("/status")
    public Result<Map<String, Object>> status() {
        Map<String, Object> r = new LinkedHashMap<>();
        r.put("buffered", collector.bufferedCount());
        r.put("totalCollected", collector.totalCollected());
        r.put("totalFlushed", collector.totalFlushed());
        r.put("lastFlushTime", collector.lastFlushTime());
        r.put("lastFlushPath", collector.lastFlushPath());
        r.put("hdfsAvailable", hdfsService.isAvailable());
        return Result.success(r);
    }
}
