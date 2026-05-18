package com.nailong.gengdirection.analytics.controller;

import com.nailong.gengdirection.analytics.dto.ActionCountVO;
import com.nailong.gengdirection.analytics.dto.HotPostVO;
import com.nailong.gengdirection.analytics.service.AnalyticsService;
import com.nailong.gengdirection.common.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 实验四 展示层 REST 接口：把 MapReduce 结果以 RESTful 方式提供给前端 ECharts。
 *
 * - GET /analytics/hot-posts?topN=15      热门帖子 Top-N（柱状图）
 * - GET /analytics/action-distribution    行为类型分布（饼图）
 * - GET /analytics/status                 数据是否就绪
 */
@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/hot-posts")
    public Result<List<HotPostVO>> hotPosts(@RequestParam(defaultValue = "15") int topN) {
        return Result.success(analyticsService.hotPosts(topN));
    }

    @GetMapping("/action-distribution")
    public Result<List<ActionCountVO>> actionDistribution() {
        return Result.success(analyticsService.actionDistribution());
    }

    @GetMapping("/status")
    public Result<Map<String, Object>> status() {
        return Result.success(analyticsService.status());
    }
}
