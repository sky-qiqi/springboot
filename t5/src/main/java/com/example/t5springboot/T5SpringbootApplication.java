package com.example.t5springboot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@MapperScan("com.example.t5springboot.mapper") // Scan for MyBatis mappers
@EnableCaching // Enable Spring Caching
public class T5SpringbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(T5SpringbootApplication.class, args);
    }

}