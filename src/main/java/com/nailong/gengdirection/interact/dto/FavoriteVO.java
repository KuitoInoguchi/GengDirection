package com.nailong.gengdirection.interact.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
