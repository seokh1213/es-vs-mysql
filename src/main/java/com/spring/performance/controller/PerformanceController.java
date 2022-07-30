package com.spring.performance.controller;

import com.spring.performance.model.vo.QueryResultVO;
import com.spring.performance.service.PostDocumentService;
import com.spring.performance.service.PostEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RequestMapping("performance")
@RestController
@RequiredArgsConstructor
public class PerformanceController {
    private final PostDocumentService postDocumentService;
    private final PostEntityService postEntityService;


    @GetMapping
    public List<QueryResultVO> checkPerformance(@RequestParam String keyword) throws IOException {
        QueryResultVO esResult = postDocumentService.searchPost("ES - default config", keyword);
        QueryResultVO mysqlLikeResult = postEntityService.searchPost("MySQL - like query", keyword, false);
        QueryResultVO mysqlIndexResult = postEntityService.searchPost("MySQL - using index", keyword, true);

        return List.of(esResult, mysqlLikeResult, mysqlIndexResult);
    }
}
