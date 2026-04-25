package com.nailong.gengdirection.post.service.impl;

import com.nailong.gengdirection.exception.GengException;
import com.nailong.gengdirection.post.entity.GengPost;
import com.nailong.gengdirection.post.mapper.PostMapper;
import com.nailong.gengdirection.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * PostService 的实现类。
 *
 * 注解说明：
 *   - @Service                   注册成 Spring Bean，让 Controller 能注入
 *   - @Slf4j                     Lombok 自动生成 log 字段
 *   - @RequiredArgsConstructor   给 final 字段自动生成构造器；
 *                                Spring 会用这个构造器自动注入 PostMapper
 *                                （这就是"构造器注入"，比 @Autowired 字段注入更推荐）
 *
 * 已实现 getById 作为参考样例，其它方法照样子补。
 */
@Slf4j
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

    // TODO: 实现 create(PostCreateDTO dto)
    //   1. 校验 title / content / authorId 非空
    //   2. new 一个 GengPost，把 DTO 字段抄过去；heatScore=0、status=2
    //   3. postMapper.insert(entity); 然后 return entity.getId()

    // TODO: 实现 pagePublished(Integer pageNum, Integer pageSize)
    //   1. 入参兜底
    //   2. offset = (pageNum-1) * pageSize
    //   3. records = postMapper.selectPublishedPage(offset, pageSize)
    //   4. total   = postMapper.countPublished()
    //   5. return PageVO.of(pageNum, pageSize, total, records)

    // TODO: 实现 deleteById(Long id)
    //   先调一次 getById(id) 确认存在，再调 postMapper.deleteById(id)
}
