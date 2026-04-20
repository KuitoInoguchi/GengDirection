package com.nailong.gengdirection.post.mapper;

import com.nailong.gengdirection.post.entity.GengPost;
import com.nailong.gengdirection.post.dto.PostVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 梗帖 Mapper 接口。
 * 标注 @Mapper 告诉 MyBatis 这是一个数据库操作接口 [cite: 14]。
 */
@Mapper
public interface PostMapper {

    /** 按主键查询单条梗帖（参考样例） [cite: 19]。 */
    GengPost selectById(@Param("id") Long id);

    /** 新增梗帖，执行后自增 ID 会回填到 post 对象中 [cite: 31]。 */
    int insert(GengPost post);

    /** 根据 ID 更新梗帖（通常配合 <set> 标签实现选择性更新） [cite: 6]。 */
    int updateById(GengPost post);

    /** 按 ID 删除记录 */
    int deleteById(@Param("id") Long id);

    /** * 分页查询已发布的梗帖，包含 JOIN 得到的作者昵称 [cite: 4, 6]。
     * 返回类型为 PostVO，直接匹配前端展示需要的数据 [cite: 10]。
     */
    List<PostVO> selectPublishedPage(@Param("offset") int offset,
                                     @Param("pageSize") int pageSize);

    /** 统计状态为“已发布”的帖子总数，用于前端分页显示 */
    long countPublished();
}