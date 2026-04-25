package com.nailong.gengdirection.tag.service.impl;

import com.nailong.gengdirection.tag.dto.TagCreateDTO;
import com.nailong.gengdirection.tag.entity.TagInfo;
import com.nailong.gengdirection.tag.mapper.TagMapper;
import com.nailong.gengdirection.tag.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagMapper tagMapper;

    @Override
    public List<TagInfo> listAllTags() {
        return tagMapper.listAllTags();
    }

    @Override
    public Long addTag(TagCreateDTO dto) {
        TagInfo tagInfo = new TagInfo();
        tagInfo.setTagName(dto.getTagName());
        tagMapper.insertTag(tagInfo);
        return tagInfo.getId();
    }

    @Override
    public int insertPostTag(Long postId, Long tagId) {
        return tagMapper.insertPostTag(postId, tagId);
    }

    @Override
    public int deletePostTag(Long postId, Long tagId) {
        return tagMapper.deletePostTag(postId, tagId);
    }

    @Override
    public List<Long> listPostIdsByTagId(Long tagId, int offset, int pageSize) {
        return tagMapper.listPostIdsByTagId(tagId, offset, pageSize);
    }
}