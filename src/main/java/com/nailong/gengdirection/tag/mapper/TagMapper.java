package com.nailong.gengdirection.tag.mapper;

import com.nailong.gengdirection.tag.entity.TagInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface TagMapper {
    // 查询所有标签
    List<TagInfo> listAllTags();

    // 新增标签
    int insertTag(TagInfo tagInfo);

    // 给帖子打标签（使用 INSERT IGNORE 避免重复）
    int insertPostTag(@Param("postId") Long postId, @Param("tagId") Long tagId);

    // 移除帖子标签
    int deletePostTag(@Param("postId") Long postId, @Param("tagId") Long tagId);

    // 按标签ID查询关联的帖子ID列表（分页）
    List<Long> listPostIdsByTagId(@Param("tagId") Long tagId,
                                  @Param("offset") int offset,
                                  @Param("pageSize") int pageSize);
}