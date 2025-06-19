package com.example.work4.repository; // 包名已更新

import com.example.work4.entity.StudentCourse; // 导入路径已更新
import com.example.work4.entity.StudentCourseId; // 导入路径已更新
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentCourseRepository extends JpaRepository<StudentCourse, StudentCourseId> {
    // Spring Data JPA will provide basic CRUD methods
    List<StudentCourse> findByStudent_StudentId(Integer studentId);
    List<StudentCourse> findByCourse_CourseId(Integer courseId);
}