package com.nailong.gengdirection.post.controller;

import com.nailong.gengdirection.common.Result;
import com.nailong.gengdirection.post.dto.PageVO;
import com.nailong.gengdirection.post.dto.PostCreateDTO;
import com.nailong.gengdirection.post.dto.PostVO;
import com.nailong.gengdirection.post.entity.GengPost;
import com.nailong.gengdirection.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/{id}")
    public Result<GengPost> getById(@PathVariable Long id) {
        return Result.success(postService.getById(id));
    }

    @GetMapping
    public Result<PageVO<PostVO>> page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(postService.pagePublished(pageNum, pageSize));
    }

    @PostMapping
    public Result<Long> create(@RequestBody PostCreateDTO postCreateDTO) {
        return Result.success(postService.create(postCreateDTO));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        postService.deleteById(id);
        return Result.success();
    }
}