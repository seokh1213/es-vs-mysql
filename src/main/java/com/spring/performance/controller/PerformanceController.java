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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RequestMapping("performance")
@RestController
@RequiredArgsConstructor
public class PerformanceController {
    private final PostDocumentService postDocumentService;
    private final PostEntityService postEntityService;


    @GetMapping
    public List<QueryResultVO> checkPerformance(@RequestParam String keyword) throws IOException, ExecutionException, InterruptedException {
        CompletableFuture<QueryResultVO> esFuture = postDocumentService.searchPost("ES - default config", keyword);

        return List.of(esFuture.get());
    }
}
