package com.nailong.gengdirection.post.service.impl;

import com.nailong.gengdirection.post.dto.PageVO;
import com.nailong.gengdirection.post.dto.PostCreateDTO;
import com.nailong.gengdirection.post.dto.PostVO;
import com.nailong.gengdirection.post.entity.GengPost;
import com.nailong.gengdirection.post.mapper.PostMapper;
import com.nailong.gengdirection.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostMapper postMapper;


    @Override
    public GengPost getById(Long id) {
        return postMapper.selectById(id);
    }

    @Override
    public Long create(PostCreateDTO dto) {
        Assert.notNull(dto, "post payload must not be null");

        GengPost post = new GengPost();
        BeanUtils.copyProperties(dto, post);
        postMapper.insert(post);
        return post.getId();
    }

    @Override
    public PageVO<PostVO> pagePublished(Integer pageNum, Integer pageSize) {
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
        List<PostVO> records = postMapper.selectPublishedPage(offset, pageSize);
        long total = postMapper.countPublished();
        return PageVO.of(pageNum, pageSize, total, records);
    }

    @Override
    public void deleteById(Long id) {
        postMapper.deleteById(id);
    }
}