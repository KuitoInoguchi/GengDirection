package com.nailong.gengdirection.interact.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 评论实体类（一对一对应数据库表 post_comment）
 *
 * 注意：
 * 1. 字段类型尽量和数据库列类型匹配（DATETIME -> LocalDateTime, BIGINT -> Long 等）。
 * 2. 数据库列是下划线命名（post_id），Java 字段用驼峰（postId）。
 *    因为 application.properties 里设置了
 *      mybatis.configuration.map-underscore-to-camel-case=true
 *    所以 MyBatis 会自动把 post_id -> postId，不需要手写 alias。
 * 3. @Data 是 Lombok 注解，编译期自动生成 getter/setter/toString/equals/hashCode。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostComment {

    /** 主键 */
    private Long id;

    /** 所属帖子 ID（外键 -> geng_post.id） */
    private Long postId;

    /** 评论人 ID（外键 -> user_info.id） */
    private Long userId;

    /** 评论内容 */
    private String content;

    /** 创建时间（数据库默认 CURRENT_TIMESTAMP） */
    private LocalDateTime createdAt;
}