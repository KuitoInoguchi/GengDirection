package com.nailong.gengdirection.tag.controller;

import com.nailong.gengdirection.common.Result;
import com.nailong.gengdirection.tag.entity.TagInfo;
import com.nailong.gengdirection.tag.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    /**
     * 查询所有标签（已实现接口，用Result<T>统一包装，符合需求要求）
     */
    @GetMapping
    public Result<List<TagInfo>> listAllTags() {
        List<TagInfo> tagList = tagService.listAllTags();
        return Result.success(tagList);
    }
}