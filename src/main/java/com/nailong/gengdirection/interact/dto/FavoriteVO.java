package com.nailong.gengdirection.interact.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 收藏返回对象（View Object）
 * 
 * 用于返回用户收藏列表中的每一项，包含帖子完整信息和收藏元数据。
 * 
 * 数据来源：
 * - 帖子信息来自 geng_post 表
 * - 作者昵称来自 user_info 表（需要 JOIN 查询）
 * - 收藏时间来自 user_favorite_post 表
 * 
 * Lombok 注解说明：
 * - @Data：自动生成 getter/setter/toString/equals/hashCode
 * - @NoArgsConstructor：自动生成无参构造器
 * - @AllArgsConstructor：自动生成全参构造器
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteVO {

    // ========== 帖子基本信息（来自 geng_post 表）==========

    /** 帖子 ID */
    private Long postId;

    /** 帖子标题 */
    private String title;

    /** 帖子内容（可以做截断处理） */
    private String content;

    /** 原帖来源 URL */
    private String sourceUrl;

    /** 作者 ID */
    private Long authorId;

    /** 作者昵称（JOIN user_info 获取） */
    private String authorNickname;

    /** 帖子热度 */
    private Integer heatScore;

    /** 帖子状态 */
    private Integer status;

    /** 帖子创建时间 */
    private LocalDateTime postCreatedAt;

    // ========== 收藏相关信息（来自 user_favorite_post 表）==========

    /** 收藏时间（用户何时收藏的） */
    private LocalDateTime favoriteTime;
}