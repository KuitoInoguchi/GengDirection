package com.nailong.gengdirection.post.mapper;

import com.nailong.gengdirection.post.entity.GengPost;
import com.nailong.gengdirection.post.dto.PostVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 梗帖 Mapper 接口。
 */
@Mapper
public interface PostMapper {

    /** 按主键查询单条梗帖 */
    GengPost selectById(@Param("id") Long id);

    /** 新增梗帖，执行后自增 ID 会回填到 post 对象中 */
    int insert(GengPost post);

    /** 按 ID 删除记录 */
    int deleteById(@Param("id") Long id);

    /** 分页查询已发布的梗帖，JOIN 获取作者昵称；tagId 为 null 时不过滤分类 */
    List<PostVO> selectPublishedPage(@Param("offset") int offset,
                                     @Param("pageSize") int pageSize,
                                     @Param("tagId") Long tagId);

    /** 统计状态为"已发布"的帖子总数；tagId 为 null 时不过滤分类 */
    long countPublished(@Param("tagId") Long tagId);
}
