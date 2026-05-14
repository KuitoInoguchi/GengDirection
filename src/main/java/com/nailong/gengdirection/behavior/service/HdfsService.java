package com.nailong.gengdirection.behavior.service;

import com.nailong.gengdirection.behavior.entity.BehaviorEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PreDestroy;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 实验四核心代码之一：用 Hadoop FileSystem Java API 直接读写 HDFS。
 *
 * 设计要点（答辩讲解用）：
 * 1. {@link FileSystem} 实例懒加载并缓存，第一次用到才连接 NameNode；
 * 2. 写入：按天分目录 /geng/behavior/raw/yyyyMMdd/events-<时间戳>.log，
 *    每行一条 TSV，方便 MapReduce 直接读；
 * 3. 读取：把 MapReduce 输出目录里的 part-r-* 全部读成文本行，交给分析层解析；
 * 4. NAT 环境关键参数 dfs.client.use.datanode.hostname=true，
 *    让客户端用主机名(master)而不是内网 IP 去连 DataNode。
 */
@Slf4j
@Service
public class HdfsService {

    private static final DateTimeFormatter DAY_FMT = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Value("${hadoop.fs-default-fs}")
    private String fsDefaultFs;

    @Value("${hadoop.user:root}")
    private String hadoopUser;

    @Value("${hadoop.behavior.raw-dir:/geng/behavior/raw}")
    private String rawDir;

    /** 懒加载并缓存的 FileSystem（线程安全，Hadoop 自身保证） */
    private volatile FileSystem fileSystem;

    private synchronized FileSystem fs() throws IOException, InterruptedException {
        if (fileSystem == null) {
            Configuration conf = new Configuration();
            conf.set("fs.defaultFS", fsDefaultFs);
            // NAT 环境：服务器对外是公网 IP，DataNode 注册的是内网/主机名，
            // 必须让客户端按主机名访问 DataNode（配合 Windows hosts: 218.194.47.8 master）
            conf.set("dfs.client.use.datanode.hostname", "true");
            // 集群不可用时尽快失败，不要长时间卡住业务线程
            conf.set("ipc.client.connect.max.retries", "1");
            conf.set("ipc.client.connect.max.retries.on.timeouts", "1");
            conf.set("ipc.client.connect.timeout", "3000");
            // 单机伪分布式只有 1 个副本
            conf.set("dfs.replication", "1");
            fileSystem = FileSystem.get(URI.create(fsDefaultFs), conf, hadoopUser);
            log.info("[HDFS] FileSystem 已连接: {} (user={})", fsDefaultFs, hadoopUser);
        }
        return fileSystem;
    }

    /**
     * 把一批行为事件写入 HDFS（一次写一个文件，避免对同一文件并发 append）。
     *
     * @return 写入的 HDFS 文件路径
     */
    public String writeBatch(List<BehaviorEvent> events) throws IOException, InterruptedException {
        if (events == null || events.isEmpty()) {
            return null;
        }
        String day = LocalDate.now().format(DAY_FMT);
        Path file = new Path(rawDir + "/" + day + "/events-" + System.currentTimeMillis() + ".log");
        FileSystem fs = fs();
        fs.mkdirs(file.getParent());
        StringBuilder sb = new StringBuilder();
        for (BehaviorEvent e : events) {
            sb.append(e.toTsvLine()).append('\n');
        }
        try (FSDataOutputStream out = fs.create(file, true)) {
            out.write(sb.toString().getBytes(StandardCharsets.UTF_8));
        }
        log.info("[HDFS] 已写入 {} 条行为事件 -> {}", events.size(), file);
        return file.toString();
    }

    /**
     * 读取一个目录下所有 part-r-* 文件（或单个文件）的全部文本行。
     * 供分析层读取 MapReduce 的输出结果。
     */
    public List<String> readLines(String pathStr) throws IOException, InterruptedException {
        List<String> lines = new ArrayList<>();
        FileSystem fs = fs();
        Path path = new Path(pathStr);
        if (!fs.exists(path)) {
            return lines;
        }
        List<Path> targets = new ArrayList<>();
        if (fs.getFileStatus(path).isDirectory()) {
            for (FileStatus st : fs.listStatus(path)) {
                String name = st.getPath().getName();
                if (st.isFile() && (name.startsWith("part-") || name.endsWith(".log"))) {
                    targets.add(st.getPath());
                }
            }
        } else {
            targets.add(path);
        }
        for (Path p : targets) {
            try (FSDataInputStream in = fs.open(p);
                 BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (!line.isBlank()) {
                        lines.add(line);
                    }
                }
            }
        }
        return lines;
    }

    /** HDFS 是否可用（连通性探活，给 /behavior/status 用） */
    public boolean isAvailable() {
        try {
            return fs().exists(new Path("/"));
        } catch (Exception e) {
            log.warn("[HDFS] 探活失败: {}", e.getMessage());
            return false;
        }
    }

    /** 某路径是否存在（判断 MapReduce 是否已经跑出结果） */
    public boolean exists(String pathStr) {
        try {
            return fs().exists(new Path(pathStr));
        } catch (Exception e) {
            return false;
        }
    }

    @PreDestroy
    public void close() {
        if (fileSystem != null) {
            try {
                fileSystem.close();
            } catch (IOException ignored) {
            }
        }
    }
}
