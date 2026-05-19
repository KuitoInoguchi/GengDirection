package com.nailong.gengdirection.analytics.service;

import com.nailong.gengdirection.analytics.dto.ActionCountVO;
import com.nailong.gengdirection.analytics.dto.HotPostVO;
import com.nailong.gengdirection.behavior.service.HdfsService;
import com.nailong.gengdirection.post.entity.GengPost;
import com.nailong.gengdirection.post.mapper.PostMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 实验四 展示层（后端）：读取 MapReduce 在 HDFS 上的输出，转成前端图表需要的数据。
 *
 * 关键点：分析结果（热度分）来自 MapReduce，帖子标题来自 MySQL，
 * 在这里做一次 join —— 这正是"大数据平台 + 业务系统融合"的体现。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final HdfsService hdfsService;
    private final PostMapper postMapper;

    @Value("${hadoop.analytics.hotpost-dir}")
    private String hotpostDir;

    @Value("${hadoop.analytics.action-dir}")
    private String actionDir;

    /** 热门帖子 Top-N：读 MR 输出 postId\theat，再补标题 */
    public List<HotPostVO> hotPosts(int topN) {
        List<HotPostVO> result = new ArrayList<>();
        try {
            for (String line : hdfsService.readLines(hotpostDir)) {
                String[] f = line.split("\t");
                if (f.length < 2) {
                    continue;
                }
                long postId = Long.parseLong(f[0].trim());
                int heat = Integer.parseInt(f[1].trim());
                String title = "帖子#" + postId;
                try {
                    GengPost p = postMapper.selectById(postId);
                    if (p != null && p.getTitle() != null) {
                        title = p.getTitle();
                    }
                } catch (Exception ignore) {
                    // 帖子可能已删除，用占位标题，不影响图表
                }
                result.add(new HotPostVO(postId, title, heat));
                if (result.size() >= topN) {
                    break;
                }
            }
        } catch (Exception e) {
            log.warn("[分析] 读取热门帖子结果失败: {}", e.getMessage());
        }
        return result;
    }

    /** 行为类型分布：读 MR 输出 action\tcount */
    public List<ActionCountVO> actionDistribution() {
        List<ActionCountVO> result = new ArrayList<>();
        try {
            for (String line : hdfsService.readLines(actionDir)) {
                String[] f = line.split("\t");
                if (f.length < 2) {
                    continue;
                }
                result.add(new ActionCountVO(f[0].trim(), Long.parseLong(f[1].trim())));
            }
        } catch (Exception e) {
            log.warn("[分析] 读取行为分布结果失败: {}", e.getMessage());
        }
        return result;
    }

    /** 数据就绪状态（前端用来判断是否已经跑过 MapReduce，截图友好） */
    public Map<String, Object> status() {
        Map<String, Object> r = new LinkedHashMap<>();
        boolean hdfsUp = hdfsService.isAvailable();
        r.put("hdfsAvailable", hdfsUp);
        r.put("hotpostReady", hdfsUp && hdfsService.exists(hotpostDir));
        r.put("actionReady", hdfsUp && hdfsService.exists(actionDir));
        r.put("hotpostDir", hotpostDir);
        r.put("actionDir", actionDir);
        return r;
    }
}
