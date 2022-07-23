package com.spring.performance.service;

import com.spring.performance.model.es.PostDocument;
import com.spring.performance.utils.ProcessDataUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostDocumentService {
    private final ElasticsearchService elasticsearchService;
    private final ProcessDataUtils processDataUtils;

    public void insertDummyData() {
        List<PostDocument> postDocumentList = processDataUtils.getPostDocumentList();
        elasticsearchService.saveEntityAll(postDocumentList);
    }
}
