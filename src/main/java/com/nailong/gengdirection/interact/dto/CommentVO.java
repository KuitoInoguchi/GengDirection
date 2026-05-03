package com.nailong.gengdirection.interact.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentVO {
    private Long postId;
    private Long userId;

    /** 评论人昵称（需要 JOIN user_info 查询） */
    private String userNickname;

    private String content;
    private LocalDateTime createdAt;
}
