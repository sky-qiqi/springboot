package org.example.t3.service;

import org.example.t3.entity.Employee;

import java.util.List;

public interface EmployeeService {
    int add(Employee employee);
    Employee getById(int id);
    List<Employee> getAll();
    int update(Employee employee);
    int deleteById(int id);
    List<Employee> getByDeptId(int deptId);
}