package com.nailong.gengdirection.post.service.impl;

import com.nailong.gengdirection.exception.GengException;
import com.nailong.gengdirection.post.dto.PageVO;
import com.nailong.gengdirection.post.dto.PostCreateDTO;
import com.nailong.gengdirection.post.dto.PostVO;
import com.nailong.gengdirection.post.entity.GengPost;
import com.nailong.gengdirection.post.mapper.PostMapper;
import com.nailong.gengdirection.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostMapper postMapper;

    @Override
    public GengPost getById(Long id) {
        if (id == null || id <= 0) {
            throw new GengException("id 非法");
        }
        GengPost post = postMapper.selectById(id);
        if (post == null) {
            throw new GengException(404, "梗帖不存在: " + id);
        }
        return post;
    }

    @Override
    public Long create(PostCreateDTO dto) {
        if (dto == null) throw new GengException("请求体为空");
        if (!StringUtils.hasText(dto.getTitle())) throw new GengException("title 不能为空");
        if (!StringUtils.hasText(dto.getContent())) throw new GengException("content 不能为空");
        if (dto.getAuthorId() == null) throw new GengException("authorId 不能为空");

        GengPost post = new GengPost();
        BeanUtils.copyProperties(dto, post);
        post.setHeatScore(0);
        post.setStatus(2);
        postMapper.insert(post);
        return post.getId();
    }

    @Override
    public PageVO<PostVO> pagePublished(Integer pageNum, Integer pageSize, Long tagId) {
        if (pageNum == null || pageNum < 1) {
            pageNum = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 10;
        }
        if (pageSize > 100) {
            pageSize = 100;
        }

        int offset = (pageNum - 1) * pageSize;
        List<PostVO> records = postMapper.selectPublishedPage(offset, pageSize, tagId);
        long total = postMapper.countPublished(tagId);
        return PageVO.of(pageNum, pageSize, total, records);
    }

    @Override
    public void deleteById(Long id) {
        getById(id);
        postMapper.deleteById(id);
    }
}
