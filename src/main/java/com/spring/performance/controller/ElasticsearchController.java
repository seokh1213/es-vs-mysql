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

}
