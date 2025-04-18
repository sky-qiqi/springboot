package org.example.t3.service.impl;

import org.example.t3.entity.Department;
import org.example.t3.mapper.DepartmentMapper;
import org.example.t3.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentMapper departmentMapper;

    @Override
    public int add(Department department) {
        return departmentMapper.insert(department);
    }

    @Override
    public Department getById(int id) {
        return departmentMapper.findById(id);
    }

    @Override
    public List<Department> getAll() {
        return departmentMapper.findAll();
    }

    @Override
    public int update(Department department) {
        return departmentMapper.update(department);
    }

    @Override
    public int deleteById(int id) {
        return departmentMapper.deleteById(id);
    }
}