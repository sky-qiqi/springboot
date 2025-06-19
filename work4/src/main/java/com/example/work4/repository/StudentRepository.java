package com.example.work4.repository; // 包名已更新

import com.example.work4.entity.Student; // 导入路径已更新
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
    // Spring Data JPA will provide basic CRUD methods
}