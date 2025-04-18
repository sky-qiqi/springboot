package org.example.t3.service;

import org.example.t3.entity.Department;

import java.util.List;

public interface DepartmentService {
    int add(Department department);
    Department getById(int id);
    List<Department> getAll();
    int update(Department department);
    int deleteById(int id);
}