package com.nailong.gengdirection.post.service;

import com.nailong.gengdirection.post.dto.PageVO;
import com.nailong.gengdirection.post.dto.PostCreateDTO;
import com.nailong.gengdirection.post.dto.PostVO;
import com.nailong.gengdirection.post.entity.GengPost;
/**
 * 梗帖业务接口。
 *
 * 接口只列方法签名 + 中文说明，具体逻辑在 PostServiceImpl。
 * 这样 Controller 只依赖接口，方便后续替换实现 / 写单测。
 *
 * 已写好 getById 作为参考样例；其它方法照样子补。
 */
public interface PostService{
    GengPost getById(Long id);

    Long create(PostCreateDTO dto);

    PageVO<PostVO> pagePublished(Integer pageNum, Integer pageSize, Long tagId);

    void deleteById(Long id);
}
