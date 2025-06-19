package com.example.work4.repository; // 确认包名是这个

import com.example.work4.entity.User; // 确认导入了 User 实体类
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    // Spring Data JPA 会自动实现基本的 CRUD 方法
    User findByUsername(String username);
}