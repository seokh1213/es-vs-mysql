package com.spring.performance.controller;

import com.spring.performance.service.PostEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RequestMapping("mysql")
@RestController
@RequiredArgsConstructor
public class MySQLController {
    private final PostEntityService postEntityService;
}
