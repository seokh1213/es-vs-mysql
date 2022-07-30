package com.spring.performance.service;

import com.spring.performance.model.mysql.PostEntity;
import com.spring.performance.model.vo.QueryResultVO;
import com.spring.performance.repository.PostRepository;
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
public class PostEntityService {
    private final ProcessDataUtils processDataUtils;
    private final PostRepository postRepository;

    @PostConstruct
    public void init() {
        Instant start = Instant.now();
        log.info("Start insert dummy data to MySQL");
        insertDummyData();
        log.info("Finished insert dummy data to MySQL, Collapsed Seconds: {}", (Instant.now().getEpochSecond() - start.getEpochSecond()));
    }

    private void insertDummyData() {
        List<PostEntity> postDocumentList = processDataUtils.getPostVOList().stream()
                .map(PostEntity::of)
                .collect(Collectors.toList());

        postRepository.saveAll(postDocumentList);
    }


    public QueryResultVO searchPost(String name, String keyword, boolean usingIndex) {
        log.info("MySQL[{}]: Start to search post entity.", name);
        long startMilliSeconds = System.currentTimeMillis();
        List<PostEntity> postEntityList;
        if (usingIndex) {
            postEntityList = postRepository.findAllByTitleIndexAndContentIndex(keyword);
        } else {
            postEntityList = postRepository.findAllByTitleContainingOrContentContaining(keyword, keyword);
        }
        long endMilliSeconds = System.currentTimeMillis();
        log.info("MySQL[{}]: End to search post entity. Took: {}", name, endMilliSeconds - startMilliSeconds);

        return QueryResultVO.builder()
                .type(name)
                .keyword(keyword)
                .tookMilliSeconds(endMilliSeconds - startMilliSeconds)
                .totalCounts(postEntityList.size())
                .build();
    }
}
