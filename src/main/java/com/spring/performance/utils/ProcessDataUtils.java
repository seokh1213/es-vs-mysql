package com.spring.performance.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.performance.model.es.PostDocument;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ProcessDataUtils {
    @Value("classpath:dummy.json")
    private Resource dummyFile;
    @Getter
    private List<PostDocument> postDocumentList;
    private final ObjectMapper objectMapper;

    @PostConstruct
    public void init() throws IOException {
        postDocumentList = objectMapper.readValue(
                dummyFile.getInputStream(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, PostDocument.class)
        );
    }
}
