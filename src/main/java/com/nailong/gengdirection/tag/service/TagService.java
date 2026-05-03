package com.nailong.gengdirection.tag.service;

import com.nailong.gengdirection.tag.dto.TagCreateDTO;
import com.nailong.gengdirection.tag.entity.TagInfo;
import java.util.List;

public interface TagService {
    List<TagInfo> listAllTags();
    Long addTag(TagCreateDTO dto);
    int insertPostTag(Long postId, Long tagId);
    int deletePostTag(Long postId, Long tagId);
    List<Long> listPostIdsByTagId(Long tagId, int offset, int pageSize);
}