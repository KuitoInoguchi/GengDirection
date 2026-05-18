package com.nailong.gengdirection.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/** 行为类型分布的一行结果（MapReduce 输出） */
@Data
@AllArgsConstructor
public class ActionCountVO {
    private String action;
    private Long count;
}
