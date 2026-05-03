package com.nailong.gengdirection.interact.service;

import com.nailong.gengdirection.interact.dto.CommentCreateDTO;
import com.nailong.gengdirection.interact.dto.CommentVO;
import com.nailong.gengdirection.post.dto.PageVO;

/**
 * 互动模块业务接口
 */
public interface InteractService {

    // ========== 评论 ==========

    /**
     * 发表评论
     * @param postId 帖子 ID
     * @param dto 评论内容
     * @return 新生成的评论 ID
     */
    Long createComment(Long postId, CommentCreateDTO dto);

    /**
     * 获取帖子评论列表
     * @param postId 帖子 ID
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 分页评论列表
     */
    PageVO<CommentVO> getCommentsByPostId(Long postId, Integer pageNum, Integer pageSize);

    /**
     * 删除评论（校验权限）
     * @param commentId 评论 ID
     * @param userId 当前用户 ID
     */
    void deleteComment(Long commentId, Long userId);

    // ========== 收藏 ==========

    /**
     * 收藏帖子（幂等）
     * @param postId 帖子 ID
     * @param userId 用户 ID
     */
    void favorite(Long postId, Long userId);

    /**
     * 取消收藏
     * @param postId 帖子 ID
     * @param userId 用户 ID
     */
    void unfavorite(Long postId, Long userId);

    /**
     * 获取用户收藏列表
     * @param userId 用户 ID
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 分页收藏列表
     */
    PageVO<?> getFavoritesByUserId(Long userId, Integer pageNum, Integer pageSize);
}
