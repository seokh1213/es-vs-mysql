package com.spring.performance.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ElasticsearchService {
    private final ElasticsearchOperations operations;

    public boolean isExistsIndex(Class<?> clazz) {
        return operations.indexOps(clazz).exists();
    }

    public boolean deleteIndex(Class<?> clazz) {
        return operations.indexOps(clazz).delete();
    }

    public boolean createIndex(Class<?> clazz) {
        return operations.indexOps(clazz).createWithMapping();
    }

    public <T> void saveEntityAll(List<T> entityList) {
        operations.save(entityList);
    }
}
