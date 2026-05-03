package com.nailong.gengdirection.interact.mapper;

import com.nailong.gengdirection.interact.dto.CommentVO;
import com.nailong.gengdirection.interact.entity.PostComment;
import com.nailong.gengdirection.post.entity.GengPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 互动模块 Mapper（数据访问层）
 * 
 * 本接口声明互动模块（评论 + 收藏）的数据库操作方法，
 * 具体 SQL 实现写在 resources/mapper/InteractMapper.xml 中。
 * 
 * 命名约定：
 * - 插入：insertXxx
 * - 查询单个：selectXxxById
 * - 查询列表：selectXxxList / selectXxxPage
 * - 统计：countXxx
 * - 删除：deleteXxxById
 * 
 * 注意：
 * 1. @Mapper 注解标识这是 MyBatis Mapper 接口
 * 2. 方法参数使用 @Param 注解命名，便于 XML 中引用
 * 3. XML 中的 namespace 必须与本接口的全限定名一致
 * 4. XML 中的 SQL id 必须与本接口的方法名一致
 */
@Mapper
public interface InteractMapper {

    // ========== 评论相关操作 ==========

    /**
     * 插入评论记录
     * 
     * @param comment 评论实体（包含 postId, userId, content）
     * @return 影响行数
     * @note XML 中配置 useGeneratedKeys="true" keyProperty="id"，
     *       插入后自增主键会自动回填到 comment.id
     */
    int insertComment(PostComment comment);

    /**
     * 按帖子 ID 分页查询评论列表
     * 
     * @param postId 帖子 ID
     * @param offset 分页偏移量
     * @param pageSize 每页大小
     * @return 评论 VO 列表（包含评论人昵称，通过 JOIN user_info 获取）
     */
    List<CommentVO> selectCommentsByPostId(
            @Param("postId") Long postId,
            @Param("offset") int offset,
            @Param("pageSize") int pageSize);

    /**
     * 统计指定帖子的评论数量
     * 
     * @param postId 帖子 ID
     * @return 评论总数
     */
    long countCommentsByPostId(@Param("postId") Long postId);

    /**
     * 按 ID 删除评论
     * 
     * @param id 评论 ID
     * @return 影响行数
     */
    int deleteCommentById(@Param("id") Long id);

    /**
     * 按 ID 查询评论（用于权限校验）
     * 
     * @param id 评论 ID
     * @return 评论实体（包含 userId，用于校验操作权限）
     */
    PostComment selectCommentById(@Param("id") Long id);

    // ========== 收藏相关操作 ==========

    /**
     * 添加收藏（幂等操作）
     * 
     * @param userId 用户 ID
     * @param postId 帖子 ID
     * @return 影响行数（0 表示已存在，1 表示新增）
     * @note 使用 INSERT IGNORE 实现幂等，重复收藏不会报错
     */
    int insertFavorite(
            @Param("userId") Long userId,
            @Param("postId") Long postId);

    /**
     * 取消收藏
     * 
     * @param userId 用户 ID
     * @param postId 帖子 ID
     * @return 影响行数
     */
    int deleteFavorite(
            @Param("userId") Long userId,
            @Param("postId") Long postId);

    /**
     * 检查用户是否已收藏指定帖子
     * 
     * @param userId 用户 ID
     * @param postId 帖子 ID
     * @return 1 表示已收藏，0 表示未收藏
     */
    int existsFavorite(
            @Param("userId") Long userId,
            @Param("postId") Long postId);

    /**
     * 查询用户收藏列表
     * 
     * @param userId 用户 ID
     * @param offset 分页偏移量
     * @param pageSize 每页大小
     * @return 收藏的帖子列表（JOIN geng_post 获取帖子详情）
     */
    List<GengPost> selectFavoritesByUserId(
            @Param("userId") Long userId,
            @Param("offset") int offset,
            @Param("pageSize") int pageSize);

    /**
     * 统计用户收藏数量
     * 
     * @param userId 用户 ID
     * @return 收藏总数
     */
    long countFavoritesByUserId(@Param("userId") Long userId);
}