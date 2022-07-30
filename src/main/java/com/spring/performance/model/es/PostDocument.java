package com.spring.performance.model.es;

import com.spring.performance.model.vo.PostVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Mapping;
import org.springframework.data.elasticsearch.annotations.Setting;

import java.time.LocalDateTime;

@Document(indexName = "post")
@Setting(settingPath = "settings.json")
@Mapping(mappingPath = "mappings.json")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostDocument {
    private String author;
    private String title;
    private String content;
    private LocalDateTime createdDt;

    public static PostDocument of(PostVO postVO) {
        return PostDocument.builder()
                .author(postVO.getAuthor())
                .title(postVO.getTitle())
                .content(postVO.getContent())
                .createdDt(postVO.getCreatedDt())
                .build();
    }
}
