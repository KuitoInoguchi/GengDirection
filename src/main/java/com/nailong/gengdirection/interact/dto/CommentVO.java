package com.nailong.gengdirection.interact.dto;

import java.time.LocalDateTime;

public class CommentVO {
    private Long postId;
    private Long userId;

    /** 评论人昵称（需要 JOIN user_info 查询） */
    private String userNickname;

    private String content;
    private LocalDateTime createdAt;
}
