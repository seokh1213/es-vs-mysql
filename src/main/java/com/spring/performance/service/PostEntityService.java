package com.spring.performance.service;

import com.spring.performance.model.mysql.PostEntity;
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
}
