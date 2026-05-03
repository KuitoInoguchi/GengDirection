package com.nailong.gengdirection.post.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 梗帖实体类（对应数据库表 geng_post）
 * Entity 是数据库表在 Java 中的“镜像”，每一个字段都与数据库列对应 。
 */
@Data
public class GengPost {

    /** 主键 ID */
    private Long id;

    /** 标题 */
    private String title;

    /** 正文内容 */
    private String content;

    /** 原帖来源 URL */
    private String sourceUrl;

    /** 发帖人 ID（外键 -> user_info.id） */
    private Long authorId;

    /** 热度分数 */
    private Integer heatScore;

    /** 状态：1=草稿，2=已发布，3=已归档 */
    private Integer status;

    /** 创建时间，使用 LocalDateTime 匹配数据库 DATETIME 类型 。 */
    private LocalDateTime createdAt;

    /** 最后更新时间 */
    private LocalDateTime updatedAt;
}