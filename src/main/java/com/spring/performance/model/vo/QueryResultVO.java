package com.spring.performance.model.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class QueryResultVO {
    private final String type;
    private final String keyword;
    private final long tookMilliSeconds;
    @Builder.Default
    private final long esTookMilliSeconds = -1;
    private final long totalCounts;
}
