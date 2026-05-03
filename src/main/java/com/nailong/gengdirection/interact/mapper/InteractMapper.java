package com.nailong.gengdirection.interact.mapper;

import com.nailong.gengdirection.interact.dto.CommentVO;
import com.nailong.gengdirection.interact.entity.PostComment;
import com.nailong.gengdirection.post.entity.GengPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface InteractMapper {

    // ========== Comment Related ========
    /** 插入评论，返回自增主键 */
    int insertComment(PostComment comment);

    /** 按帖子 ID 分页查询评论（JOIN user_info 拿昵称） */
    List<CommentVO> selectCommentsByPostId(
            @Param("postId") Long postId,
            @Param("offset") int offset,
            @Param("pageSize") int pageSize);

    /** 统计帖子评论数 */
    long countCommentsByPostId(@Param("postId") Long postId);

    /** 按 ID 删除评论 */
    int deleteCommentById(@Param("id") Long id);

    /** 按 ID 查询评论（用于权限校验） */
    PostComment selectCommentById(@Param("id") Long id);

    // ========== Favorite Related ========
    /** 添加收藏（幂等：INSERT IGNORE） */
    int insertFavorite(
            @Param("userId") Long userId,
            @Param("postId") Long postId);

    /** 取消收藏 */
    int deleteFavorite(
            @Param("userId") Long userId,
            @Param("postId") Long postId);

    /** 检查是否已收藏 */
    int existsFavorite(
            @Param("userId") Long userId,
            @Param("postId") Long postId);

    /** 查询用户收藏列表 */
    List<GengPost> selectFavoritesByUserId(
            @Param("userId") Long userId,
            @Param("offset") int offset,
            @Param("pageSize") int pageSize);

    /** 统计用户收藏数 */
    long countFavoritesByUserId(@Param("userId") Long userId);
}
