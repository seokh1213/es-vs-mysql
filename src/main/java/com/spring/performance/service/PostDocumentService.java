package com.spring.performance.service;

import com.spring.performance.model.es.PostDocument;
import com.spring.performance.utils.ProcessDataUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostDocumentService {
    private final ElasticsearchService elasticsearchService;
    private final ProcessDataUtils processDataUtils;

    @PostConstruct
    public void init() {
        Instant start = Instant.now();
        log.info("Start insert dummy data to ES");
        if(elasticsearchService.isExistsIndex(PostDocument.class)) {
            boolean isDeleted = elasticsearchService.deleteIndex(PostDocument.class);
            if(!isDeleted) {
                throw new RuntimeException("Fail to delete index");
            }
        }

        boolean isSuccess = elasticsearchService.createIndex(PostDocument.class);
        if(!isSuccess) {
            throw new RuntimeException("Fail to create index");
        }

        insertDummyData();

        log.info("Finished insert dummy data to ES, Collapsed Seconds: {}", (Instant.now().getEpochSecond() - start.getEpochSecond()));
    }

    private void insertDummyData() {
        List<PostDocument> postDocumentList = processDataUtils.getPostVOList().stream()
                .map(PostDocument::of)
                .collect(Collectors.toList());
        elasticsearchService.saveEntityAll(postDocumentList);
    }
}
