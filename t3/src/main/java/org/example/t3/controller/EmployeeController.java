package org.example.t3.controller;

import org.example.t3.entity.Employee;
import org.example.t3.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @PostMapping
    public int addEmployee(@RequestBody Employee employee) {
        return employeeService.add(employee);
    }

    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable int id) {
        return employeeService.getById(id);
    }

    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.getAll();
    }

    @PutMapping("/{id}")
    public int updateEmployee(@PathVariable int id, @RequestBody Employee employee) {
        employee.setEmpId(id);
        return employeeService.update(employee);
    }

    @DeleteMapping("/{id}")
    public int deleteEmployee(@PathVariable int id) {
        return employeeService.deleteById(id);
    }

    @GetMapping("/department/{deptId}")
    public List<Employee> getEmployeesByDeptId(@PathVariable int deptId) {
        return employeeService.getByDeptId(deptId);
    }
}