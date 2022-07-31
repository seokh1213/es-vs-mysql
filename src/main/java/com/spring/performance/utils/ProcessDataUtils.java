package com.spring.performance.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.performance.model.vo.PostVO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ProcessDataUtils {
    @Value("classpath:dummy.json")
    private Resource dummyFile;
    @Getter
    private List<PostVO> postVOList;
    private final ObjectMapper objectMapper;

    @PostConstruct
    public void init() throws IOException {
        postVOList = objectMapper.readValue(
                dummyFile.getInputStream(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, PostVO.class)
        );
    }

    public List<PostVO> getSamplingData(int start, int end) {
        final int length = postVOList.size();
        List<PostVO> data = new ArrayList<>();

        for (int i = 0; i < Math.ceil(((double) end - start) / length); i++) {
            if (end - start - i * length < length) {
                data.addAll(postVOList.subList(0, (end - start) % length));
            } else {
                data.addAll(postVOList);
            }
        }

        return data;
    }
}
