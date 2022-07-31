package com.spring.performance.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.TrackHits;
import com.spring.performance.model.es.PostDocument;
import com.spring.performance.model.vo.QueryResultVO;
import com.spring.performance.utils.ProcessDataUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostDocumentService {
    private final ElasticsearchService elasticsearchService;
    private final ProcessDataUtils processDataUtils;
    private final ElasticsearchClient elasticsearchClient;

    public void init(int samplingCount) {
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

        final int chunk = 10_000;
        for (int i = 0; i < Math.ceil(((double) samplingCount) / chunk); i++) {
            log.info("Elasticsearch - Insert Data {} / {}", i + 1, (int) Math.ceil(((double) samplingCount) / chunk));
            insertDummyData(i * chunk, Math.min((i + 1) * chunk, samplingCount));
        }

        log.info("Finished insert dummy data to ES, Collapsed Seconds: {}", (Instant.now().getEpochSecond() - start.getEpochSecond()));
    }

    private void insertDummyData(int start, int end) {
        List<PostDocument> postDocumentList = processDataUtils.getSamplingData(start, end).stream()
                .map(PostDocument::of)
                .collect(Collectors.toList());
        elasticsearchService.saveEntityAll(postDocumentList);
    }

    public QueryResultVO searchPost(String name, String keyword) throws IOException {
        log.info("ES[{}]: Start to search post document.", name);
        long startMilliSeconds = System.currentTimeMillis();
        SearchResponse<PostDocument> searchResponse = elasticsearchClient.search(
                builder -> builder
                        .requestCache(false)
                        .query(titleAndContentQuery(keyword))
                        .source(s -> s.filter(f -> f.excludes("_class")))
                        .trackTotalHits(new TrackHits.Builder().enabled(true).build())
                        .size(0),
                PostDocument.class
        );
        long endMilliSeconds = System.currentTimeMillis();
        log.info("ES[{}]: Finish to search post document. Took: {}({})", name, endMilliSeconds - startMilliSeconds, searchResponse.took());

        assert searchResponse.hits().total() != null;

        return QueryResultVO.builder()
                .type(name)
                .keyword(keyword)
                .tookMilliSeconds(endMilliSeconds - startMilliSeconds)
                .esTookMilliSeconds(searchResponse.took())
                .totalCounts(searchResponse.hits().total().value())
                .build();
    }

    public Query titleAndContentQuery(String keyword) {
        Query byTitle = MatchQuery.of(m -> m.field("title").query(keyword))._toQuery();
        Query byContent = MatchQuery.of(m -> m.field("content").query(keyword))._toQuery();

        return Query.of(q -> q.bool(b -> b.should(byTitle, byContent)));
    }

}
