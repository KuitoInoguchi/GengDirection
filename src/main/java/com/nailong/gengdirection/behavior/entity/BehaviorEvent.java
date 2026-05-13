package com.nailong.gengdirection.behavior.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 一条用户行为事件。这是写入 HDFS 的最小数据单元。
 *
 * 落到 HDFS 的存储格式是「一行一条、字段用 \t 分隔」的纯文本（TSV）：
 * <pre>userId \t action \t postId \t ts</pre>
 * 选 TSV 而不是 JSON，是为了让 MapReduce 的 Mapper 用 String.split("\t")
 * 就能解析，逻辑最直观、答辩最好讲。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BehaviorEvent {

    /** 用户 ID，匿名/未知时为 0 */
    private Long userId;

    /** 行为类型 */
    private ActionType action;

    /** 关联帖子 ID，无关联（如纯搜索）时为 0 */
    private Long postId;

    /** 行为发生时间（epoch 毫秒） */
    private Long ts;

    public static BehaviorEvent of(Long userId, ActionType action, Long postId) {
        return new BehaviorEvent(
                userId == null ? 0L : userId,
                action,
                postId == null ? 0L : postId,
                System.currentTimeMillis());
    }

    /** 序列化为 HDFS 中的一行 TSV 文本 */
    public String toTsvLine() {
        return (userId == null ? 0L : userId) + "\t"
                + action.name() + "\t"
                + (postId == null ? 0L : postId) + "\t"
                + (ts == null ? System.currentTimeMillis() : ts);
    }
}
