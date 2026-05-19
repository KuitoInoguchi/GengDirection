package com.nailong.gengdirection.behavior.dto;

import lombok.Data;

/**
 * 前端主动埋点上报体。
 * 例如帖子详情页停留、点击某按钮等前端才知道的行为，
 * 通过 POST /behavior/track 上报，符合 RESTful 规范。
 */
@Data
public class TrackDTO {
    /** 用户 ID，可空 */
    private Long userId;
    /** 行为类型字符串，对应 ActionType（VIEW/FAVORITE/COMMENT/CREATE_POST/SEARCH/TRACK） */
    private String action;
    /** 关联帖子 ID，可空 */
    private Long postId;
}
