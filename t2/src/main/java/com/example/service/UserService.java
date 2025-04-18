package com.example.service;


import com.example.model.User;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service // 标记这个类为一个服务组件
public class UserService {
    public User findById(Long id) {
        System.out.println("Executing findById with ID: " + id);
        if (id == 100L) {
            throw new DataAccessException("Simulated database connection failure") {}; // 模拟数据库连接失败异常
        }
        return new User(id, "User-" + id, "user" + id + "@example.com");
    }

    public int save(User user) {
        System.out.println("Executing save with User: " + user);
        if ("duplicate".equals(user.getUsername())) {
            throw new DataIntegrityViolationException("Simulated unique constraint violation"); // 模拟唯一约束冲突异常
        }
        return 1;
    }

    public int delete(Long id) {
        System.out.println("Executing delete with ID: " + id);
        if (id == 999L) {
            throw new EmptyResultDataAccessException("Simulated ID not found", 1); // 模拟 ID 不存在异常
        }
        return 1;
    }
}