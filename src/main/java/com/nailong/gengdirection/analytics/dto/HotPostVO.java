package com.nailong.gengdirection.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/** 热门帖子 Top-N 的一行结果（MapReduce 输出 + 关联 MySQL 标题） */
@Data
@AllArgsConstructor
public class HotPostVO {
    private Long postId;
    private String title;
    private Integer heatScore;
}
