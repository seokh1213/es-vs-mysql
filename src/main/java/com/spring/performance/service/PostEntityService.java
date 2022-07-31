package com.spring.performance.service;

import com.spring.performance.model.mysql.PostEntity;
import com.spring.performance.model.vo.QueryResultVO;
import com.spring.performance.repository.PostRepository;
import com.spring.performance.utils.ProcessDataUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostEntityService {
    private final ProcessDataUtils processDataUtils;
    private final PostRepository postRepository;

    private final Flyway flyway;

    public void init(int samplingCount) {
        Instant start = Instant.now();
        log.info("Start insert dummy data to MySQL");
        flyway.migrate(); // execute "resources/db/migration"
        final int chunk = 10_000;
        for (int i = 0; i < Math.ceil(((double) samplingCount) / chunk); i++) {
            log.info("MySQL - Insert Data {} / {}", i + 1, (int) Math.ceil(((double) samplingCount) / chunk));
            insertDummyData(i * chunk, Math.min((i + 1) * chunk, samplingCount));
        }
        log.info("Finished insert dummy data to MySQL, Collapsed Seconds: {}", (Instant.now().getEpochSecond() - start.getEpochSecond()));
    }

    private void insertDummyData(int start, int end) {
        List<PostEntity> postDocumentList = processDataUtils.getSamplingData(start, end).stream()
                .map(PostEntity::of)
                .collect(Collectors.toList());

        postRepository.saveAll(postDocumentList);
    }


    public QueryResultVO searchPost(String name, String keyword, boolean usingIndex) {
        log.info("MySQL[{}]: Start to search post entity.", name);
        long startMilliSeconds = System.currentTimeMillis();

        int totalCounts;
        if (usingIndex) {
            totalCounts = postRepository.countAllByTitleIndexAndContentIndex(keyword);
        } else {
            totalCounts = postRepository.countAllByTitleContainingOrContentContaining(keyword, keyword);
        }
        long endMilliSeconds = System.currentTimeMillis();
        log.info("MySQL[{}]: End to search post entity. Took: {}", name, endMilliSeconds - startMilliSeconds);

        return QueryResultVO.builder()
                .type(name)
                .keyword(keyword)
                .tookMilliSeconds(endMilliSeconds - startMilliSeconds)
                .totalCounts(totalCounts)
                .build();
    }
}
