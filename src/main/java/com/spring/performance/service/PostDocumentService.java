package com.spring.performance.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.spring.performance.model.es.PostDocument;
import com.spring.performance.model.vo.QueryResultVO;
import com.spring.performance.utils.ProcessDataUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostDocumentService {
    private final ElasticsearchService elasticsearchService;
    private final ProcessDataUtils processDataUtils;
    private final ElasticsearchClient elasticsearchClient;

    @PostConstruct
    public void init() {
        Instant start = Instant.now();
        log.info("Start insert dummy data to ES");
        if (elasticsearchService.isExistsIndex(PostDocument.class)) {
            boolean isDeleted = elasticsearchService.deleteIndex(PostDocument.class);
            if (!isDeleted) {
                throw new RuntimeException("Fail to delete index");
            }
        }

        boolean isSuccess = elasticsearchService.createIndex(PostDocument.class);
        if (!isSuccess) {
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

    @Async
    public CompletableFuture<QueryResultVO> searchPost(String name, String keyword) throws IOException {
        int MAX_SIZE = 10_000;
        SearchResponse<PostDocument> searchResponse = elasticsearchClient.search(
                builder -> builder
                        .requestCache(false)
                        .query(titleAndContentQuery(keyword))
                        .source(s -> s.filter(f -> f.excludes("_class")))
                        .size(MAX_SIZE),
                PostDocument.class
        );

        assert searchResponse.hits().total() != null;

        return CompletableFuture.completedFuture(
                QueryResultVO.builder()
                        .type(name)
                        .keyword(keyword)
                        .tookSeconds(searchResponse.took())
                        .totalCounts(searchResponse.hits().total().value())
                        .build()
        );
    }

    public Query titleAndContentQuery(String keyword) {
        Query byTitle = MatchQuery.of(m -> m.field("title").query(keyword))._toQuery();
        Query byContent = MatchQuery.of(m -> m.field("content").query(keyword))._toQuery();

        return Query.of(q -> q.bool(b -> b.should(byTitle, byContent)));
    }

}
