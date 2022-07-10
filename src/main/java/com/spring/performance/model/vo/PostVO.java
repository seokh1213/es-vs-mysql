package com.spring.performance.model.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@RequiredArgsConstructor
public class PostVO {
    private final String author;
    private final String title;
    private final String content;
    private final LocalDateTime createdDt;
}
