package com.gzu.ssmdemo.config;

import org.springframework.context.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan(value = "com.gzu.ssmdemo.service") // 扫描 Service 包
@PropertySource("classpath:jdbc.properties")
@Import({JdbcConfig.class, MybatisConfig.class}) // 导入其他配置类
@EnableTransactionManagement // 启用事务管理
public class SpringConfig {
}
