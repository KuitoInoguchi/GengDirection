package com.nailong.gengdirection.interact.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 评论创建请求 DTO（Data Transfer Object）
 * 
 * 用于接收前端创建评论的请求参数，不直接使用实体类作为请求体。
 * 
 * Lombok 注解说明：
 * - @Data：自动生成 getter/setter/toString/equals/hashCode
 * - @NoArgsConstructor：自动生成无参构造器
 * - @AllArgsConstructor：自动生成全参构造器
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentCreateDTO {

    /** 评论内容（必填） */
    private String content;

    /** 
     * 评论人 ID（必填）
     * 
     * 临时方案：当前需要前端传入 userId
     * 后续优化：登录功能完善后，从 Session/token 中自动获取
     */
    private Long userId;
}