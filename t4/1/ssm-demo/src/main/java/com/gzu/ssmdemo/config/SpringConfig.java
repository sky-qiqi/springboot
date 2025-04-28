package com.gzu.ssmdemo.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan(value = "com.gzu.ssmdemo",
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, value = Controller.class)
)
@EnableTransactionManagement // 启用事务管理
@MapperScan("com.gzu.ssmdemo.mapper")
@PropertySource("classpath:jdbc.properties")
public class SpringConfig {
}
