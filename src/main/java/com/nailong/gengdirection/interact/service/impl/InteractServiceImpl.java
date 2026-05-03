package com.nailong.gengdirection.interact.service.impl;

import com.nailong.gengdirection.exception.GengException;
import com.nailong.gengdirection.interact.dto.CommentCreateDTO;
import com.nailong.gengdirection.interact.dto.CommentVO;
import com.nailong.gengdirection.interact.entity.PostComment;
import com.nailong.gengdirection.interact.mapper.InteractMapper;
import com.nailong.gengdirection.interact.service.InteractService;
import com.nailong.gengdirection.post.dto.PageVO;
import com.nailong.gengdirection.post.mapper.PostMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InteractServiceImpl implements InteractService {

    private final InteractMapper interactMapper;
    private final PostMapper postMapper;

    // ========== 评论实现 ==========

    @Override
    public Long createComment(Long postId, CommentCreateDTO dto) {
        // 参数校验
        if (postId == null || postId <= 0) {
            throw new GengException("帖子 ID 非法");
        }
        if (dto.getUserId() == null || dto.getUserId() <= 0) {
            throw new GengException("用户 ID 非法");
        }
        if (dto.getContent() == null || dto.getContent().trim().isEmpty()) {
            throw new GengException("评论内容不能为空");
        }

        // 构建实体
        PostComment comment = new PostComment();
        comment.setPostId(postId);
        comment.setUserId(dto.getUserId());
        comment.setContent(dto.getContent());

        // 插入并返回主键
        interactMapper.insertComment(comment);
        return comment.getId();
    }

    @Override
    public PageVO<CommentVO> getCommentsByPostId(Long postId, Integer pageNum, Integer pageSize) {
        // 参数兜底
        if (pageNum == null || pageNum < 1) pageNum = 1;
        if (pageSize == null || pageSize < 1) pageSize = 20;
        if (pageSize > 100) pageSize = 100;

        int offset = (pageNum - 1) * pageSize;
        List<CommentVO> records = interactMapper.selectCommentsByPostId(postId, offset, pageSize);
        long total = interactMapper.countCommentsByPostId(postId);

        return PageVO.of(pageNum, pageSize, total, records);
    }

    @Override
    public void deleteComment(Long commentId, Long userId) {
        if (commentId == null || commentId <= 0) {
            throw new GengException("评论 ID 非法");
        }

        // 查询评论确认存在并校验权限
        PostComment comment = interactMapper.selectCommentById(commentId);
        if (comment == null) {
            throw new GengException(404, "评论不存在");
        }
        if (!comment.getUserId().equals(userId)) {
            throw new GengException(403, "无权删除他人评论");
        }

        interactMapper.deleteCommentById(commentId);
    }

    // ========== 收藏实现 ==========

    @Override
    public void favorite(Long postId, Long userId) {
        if (postId == null || postId <= 0) {
            throw new GengException("帖子 ID 非法");
        }
        if (userId == null || userId <= 0) {
            throw new GengException("用户 ID 非法");
        }
        
        // 校验帖子是否存在
        if (postMapper.selectById(postId) == null) {
            throw new GengException(404, "帖子不存在: " + postId);
        }
        
        // INSERT IGNORE 保证幂等（重复收藏不报错）
        interactMapper.insertFavorite(userId, postId);
    }

    @Override
    public void unfavorite(Long postId, Long userId) {
        if (postId == null || postId <= 0) {
            throw new GengException("帖子 ID 非法");
        }
        if (userId == null || userId <= 0) {
            throw new GengException("用户 ID 非法");
        }
        
        // 校验帖子是否存在
        if (postMapper.selectById(postId) == null) {
            throw new GengException(404, "帖子不存在: " + postId);
        }
        
        interactMapper.deleteFavorite(userId, postId);
    }

    @Override
    public PageVO<?> getFavoritesByUserId(Long userId, Integer pageNum, Integer pageSize) {
        if (userId == null || userId <= 0) {
            throw new GengException("用户 ID 非法");
        }
        if (pageNum == null || pageNum < 1) pageNum = 1;
        if (pageSize == null || pageSize < 1) pageSize = 20;
        if (pageSize > 100) pageSize = 100;

        int offset = (pageNum - 1) * pageSize;
        List<?> records = interactMapper.selectFavoritesByUserId(userId, offset, pageSize);
        long total = interactMapper.countFavoritesByUserId(userId);

        return PageVO.of(pageNum, pageSize, total, records);
    }
}
