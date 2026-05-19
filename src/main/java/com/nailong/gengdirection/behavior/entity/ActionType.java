package com.nailong.gengdirection.behavior.entity;

/**
 * 用户行为类型。
 *
 * weight 是该行为对"帖子热度"的贡献权重，MapReduce 的 HotPostJob 会用到：
 * 浏览最廉价(1)，发帖(2)，评论(3)，收藏价值最高(5)。
 * 把权重定义在这里，前后端 + MR 三处口径一致，答辩时一句话能讲清。
 */
public enum ActionType {

    VIEW(1),         // 浏览帖子详情
    FAVORITE(5),     // 收藏帖子
    COMMENT(3),      // 评论帖子
    CREATE_POST(2),  // 发布新帖
    SEARCH(1),       // 按标签筛选/搜索
    TRACK(1);        // 前端主动上报的通用埋点

    private final int weight;

    ActionType(int weight) {
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }

    /** 宽松解析：非法值归为 TRACK，保证采集链路不因脏数据中断。 */
    public static ActionType safeOf(String name) {
        if (name == null) {
            return TRACK;
        }
        try {
            return ActionType.valueOf(name.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return TRACK;
        }
    }
}
