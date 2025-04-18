package com.example.config;


import com.example.service.UserService;
import com.example.aop.ServiceLoggingAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration // 标记这个类为一个配置类
@ComponentScan("com.example") // 告诉 Spring 扫描 com.example 包下的组件
@EnableAspectJAutoProxy // 启用 AspectJ 自动代理功能
public class AppConfig {

    @Bean // 将 UserService 实例化为 Spring 管理的 Bean
    public UserService userService() {
        return new UserService();
    }

    @Bean // 将 com.example.aop.ServiceLoggingAspect 实例化为 Spring 管理的 Bean
    public ServiceLoggingAspect serviceLoggingAspect() {
        return new ServiceLoggingAspect();
    }
}