package com.nailong.gengdirection.interact.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 评论返回对象（View Object）
 * 
 * 用于将评论信息返回给前端，相比实体类 PostComment，增加了额外的关联信息。
 * 
 * 设计说明：
 * 1. userNickname 需要通过 JOIN user_info 表查询获取
 * 2. 不直接返回实体类，避免暴露不必要的字段
 * 3. Lombok 注解自动生成 getter/setter 等方法
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentVO {

    /** 所属帖子 ID */
    private Long postId;

    /** 评论人 ID */
    private Long userId;

    /** 评论人昵称（需要 JOIN user_info 表查询获取） */
    private String userNickname;

    /** 评论内容 */
    private String content;

    /** 评论创建时间 */
    private LocalDateTime createdAt;
}