package com.spring.performance.controller;

import com.spring.performance.service.ElasticsearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
//@RestController
@RequestMapping("es")
public class ElasticsearchController {
    private final ElasticsearchService elasticsearchService;

    @GetMapping("create/post")
    public String createPostIndex() {
        elasticsearchService.deletePostIndex();
        return "success";
    }

}
