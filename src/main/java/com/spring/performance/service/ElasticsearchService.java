package com.spring.performance.service;

import com.spring.performance.model.es.PostDocument;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;


//@Service
@RequiredArgsConstructor
public class ElasticsearchService {
    private final ElasticsearchOperations operations;

    public void deletePostIndex() {
        Query query = new NativeSearchQueryBuilder().withQuery(QueryBuilders.matchAllQuery()).build();
        String[] ids = operations.search(query, PostDocument.class)
                .stream()
                .map(SearchHit::getId)
                .toArray(String[]::new);

        if (ids.length == 0) {
            return;
        }

        Query idsQuery = new NativeSearchQueryBuilder().withQuery(QueryBuilders.idsQuery().addIds(ids)).build();
        operations.delete(idsQuery, PostDocument.class);
    }

    public void createPostIndex() {


    }
}
