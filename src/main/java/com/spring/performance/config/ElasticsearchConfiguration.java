package com.spring.performance.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Configuration
public class ElasticsearchConfiguration {

    @Bean
    public ElasticsearchTransport elasticsearchTransport(RestClient restClient) {
        JacksonJsonpMapper jsonMapper = new JacksonJsonpMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer(LocalDateTime.class, new JsonDeserializer<>() {
            @Override
            public LocalDateTime deserialize(JsonParser p, DeserializationContext context) throws IOException {
                long value = p.getValueAsLong();
                Instant instant = Instant.ofEpochMilli(value);
                return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
            }
        });

        jsonMapper.objectMapper().registerModule(javaTimeModule);

        return new RestClientTransport(restClient, jsonMapper);
    }

    @Bean
    public ElasticsearchClient elasticsearchClient(ElasticsearchTransport elasticsearchTransport) {
        return new ElasticsearchClient(elasticsearchTransport);
    }
}
