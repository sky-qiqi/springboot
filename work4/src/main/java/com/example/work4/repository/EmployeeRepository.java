package com.example.work4.repository; // 包名已更新

import com.example.work4.entity.Employee; // 导入路径已更新
import com.example.work4.entity.Department; // 导入路径已更新
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    // Spring Data JPA will provide basic CRUD methods
    List<Employee> findByDepartment(Department department);
    List<Employee> findByDepartment_DeptId(Integer deptId);
}