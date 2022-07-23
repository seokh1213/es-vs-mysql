package com.spring.performance.service;

import com.spring.performance.model.mysql.PostEntity;
import com.spring.performance.repository.PostRepository;
import com.spring.performance.utils.ProcessDataUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostEntityService {
    private final ProcessDataUtils processDataUtils;
    private final PostRepository postRepository;

    public void insertDummyData() {
        List<PostEntity> postDocumentList = processDataUtils.getPostVOList().stream()
                .map(PostEntity::of)
                .collect(Collectors.toList());

        postRepository.saveAll(postDocumentList);
    }
}
