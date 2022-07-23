package com.spring.performance.controller;

import com.spring.performance.model.es.PostDocument;
import com.spring.performance.service.ElasticsearchService;
import com.spring.performance.service.PostDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RequiredArgsConstructor
@RestController
@RequestMapping("es")
public class ElasticsearchController {
    private final ElasticsearchService elasticsearchService;
    private final PostDocumentService postDocumentService;

    @GetMapping("create/post")
    public String createPostIndex() {
        Instant start = Instant.now();
        if(elasticsearchService.isExistsIndex(PostDocument.class)) {
            boolean isDeleted = elasticsearchService.deleteIndex(PostDocument.class);
            if(!isDeleted) {
                return "fail";
            }
        }

        boolean isSuccess = elasticsearchService.createIndex(PostDocument.class);
        if(!isSuccess) {
            return "fail";
        }

        postDocumentService.insertDummyData();

        return "Success, Collapsed Time(Second): " + (Instant.now().getEpochSecond() - start.getEpochSecond());
    }

}
