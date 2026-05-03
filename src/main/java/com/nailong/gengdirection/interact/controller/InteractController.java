package com.nailong.gengdirection.interact.controller;

import com.nailong.gengdirection.common.Result;
import com.nailong.gengdirection.interact.dto.CommentCreateDTO;
import com.nailong.gengdirection.interact.dto.CommentVO;
import com.nailong.gengdirection.interact.service.InteractService;
import com.nailong.gengdirection.post.dto.PageVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 互动模块 REST 接口
 *
 * 接口清单：
 * POST   /posts/{postId}/comments      - 发评论
 * GET    /posts/{postId}/comments      - 评论列表（分页）
 * DELETE /comments/{id}                - 删除评论
 * POST   /posts/{postId}/favorite      - 收藏
 * DELETE /posts/{postId}/favorite      - 取消收藏
 * GET    /users/{userId}/favorites     - 我的收藏
 */
@RestController
@RequiredArgsConstructor
public class InteractController {

    private final InteractService interactService;

    // ========== 评论接口 ==========

    /**
     * 发表评论
     * POST /posts/{postId}/comments
     */
    @PostMapping("/posts/{postId}/comments")
    public Result<Long> createComment(
            @PathVariable Long postId,
            @RequestBody CommentCreateDTO dto) {
        return Result.success(interactService.createComment(postId, dto));
    }

    /**
     * 获取帖子评论列表
     * GET /posts/{postId}/comments?pageNum=1&pageSize=20
     */
    @GetMapping("/posts/{postId}/comments")
    public Result<PageVO<CommentVO>> getComments(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "20") Integer pageSize) {
        return Result.success(interactService.getCommentsByPostId(postId, pageNum, pageSize));
    }

    /**
     * 删除评论
     * DELETE /comments/{id}?userId=xxx // at the moment
     */
    @DeleteMapping("/comments/{id}")
    public Result<Void> deleteComment(
            @PathVariable Long id,
            @RequestParam Long userId) { // where is the request parameter??? (A: Just do it at the moment.)
        interactService.deleteComment(id, userId);
        return Result.success();
    }

    // ========== 收藏接口 ==========

    /**
     * 收藏帖子（幂等）
     * POST /posts/{postId}/favorite?userId=xxx // at the moment
     */
    @PostMapping("/posts/{postId}/favorite")
    public Result<Void> favorite(
            @PathVariable Long postId,
            @RequestParam Long userId) {
        interactService.favorite(postId, userId);
        return Result.success();
    }

    /**
     * 取消收藏
     * DELETE /posts/{postId}/favorite?userId=xxx // at the moment
     */
    @DeleteMapping("/posts/{postId}/favorite")
    public Result<Void> unfavorite(
            @PathVariable Long postId,
            @RequestParam Long userId) {
        interactService.unfavorite(postId, userId);
        return Result.success();
    }

    /**
     * 获取用户收藏列表
     * GET /users/{userId}/favorites?pageNum=1&pageSize=20
     */
    @GetMapping("/users/{userId}/favorites")
    public Result<PageVO<?>> getFavorites(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "20") Integer pageSize) {
        return Result.success(interactService.getFavoritesByUserId(userId, pageNum, pageSize));
    }
}
