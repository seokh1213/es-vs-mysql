package com.spring.performance.service;

import com.spring.performance.model.es.PostDocument;
import com.spring.performance.utils.ProcessDataUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostDocumentService {
    private final ElasticsearchService elasticsearchService;
    private final ProcessDataUtils processDataUtils;

    public void insertDummyData() {
        List<PostDocument> postDocumentList = processDataUtils.getPostVOList().stream()
                .map(PostDocument::of)
                .collect(Collectors.toList());
        elasticsearchService.saveEntityAll(postDocumentList);
    }
}
