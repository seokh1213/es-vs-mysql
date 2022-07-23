package com.spring.performance.model.es;

import com.spring.performance.model.vo.PostVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Mapping;
import org.springframework.data.elasticsearch.annotations.Setting;

import java.time.LocalDateTime;

@Document(indexName = "post")
@Setting(settingPath = "settings.json")
@Mapping(mappingPath = "mappings.json")
@Getter
@Builder
@AllArgsConstructor
public class PostDocument {
    private final String author;
    private final String title;
    private final String content;
    private final LocalDateTime createdDt;

    public static PostDocument of(PostVO postVO) {
        return PostDocument.builder()
                .author(postVO.getAuthor())
                .title(postVO.getTitle())
                .content(postVO.getContent())
                .createdDt(postVO.getCreatedDt())
                .build();
    }
}
