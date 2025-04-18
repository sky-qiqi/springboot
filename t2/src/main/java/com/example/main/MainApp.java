package com.example.main;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import com.example.config.AppConfig;
import com.example.model.User;
import com.example.service.UserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MainApp {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class); // 创建基于注解的 Spring 应用上下文
        UserService userService = context.getBean(UserService.class); // 从 Spring 容器中获取 UserService Bean

        // 测试正常场景
        User user1 = userService.findById(1L);
        System.out.println("findById result: " + user1);

        User userToSave = new User(null, "testUser", "test@example.com");
        int saveResult = userService.save(userToSave);
        System.out.println("save result: " + saveResult);

        int deleteResult = userService.delete(1L);
        System.out.println("delete result: " + deleteResult);

        System.out.println("\n--- 模拟异常场景 ---");

        // 测试异常场景
        try {
            userService.findById(100L);
        } catch (DataAccessException e) {
            System.out.println("Caught expected exception in findById: " + e.getMessage()); // 捕获 findById 中预期的异常
        }

        try {
            userService.save(new User(null, "duplicate", "dup@example.com"));
        } catch (DataIntegrityViolationException e) {
            System.out.println("Caught expected exception in save: " + e.getMessage()); // 捕获 save 中预期的异常
        }

        try {
            userService.delete(999L);
        } catch (EmptyResultDataAccessException e) {
            System.out.println("Caught expected exception in delete: " + e.getMessage()); // 捕获 delete 中预期的异常
        }

        context.close(); // 关闭 Spring 应用上下文
    }
}
