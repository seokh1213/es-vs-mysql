package com.spring.performance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class SpringPerformanceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringPerformanceApplication.class, args);
    }

}
