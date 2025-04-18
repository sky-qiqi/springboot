package org.example.t3.service.impl;

import org.example.t3.entity.Employee;
import org.example.t3.mapper.EmployeeMapper;
import org.example.t3.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public int add(Employee employee) {
        return employeeMapper.insert(employee);
    }

    @Override
    public Employee getById(int id) {
        return employeeMapper.findById(id);
    }

    @Override
    public List<Employee> getAll() {
        return employeeMapper.findAll();
    }

    @Override
    public int update(Employee employee) {
        return employeeMapper.update(employee);
    }

    @Override
    public int deleteById(int id) {
        return employeeMapper.deleteById(id);
    }

    @Override
    public List<Employee> getByDeptId(int deptId) {
        return employeeMapper.findByDeptId(deptId);
    }
}