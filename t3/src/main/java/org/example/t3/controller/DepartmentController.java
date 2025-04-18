package org.example.t3.controller;

import org.example.t3.entity.Department;
import org.example.t3.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/departments")
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;

    @PostMapping
    public int addDepartment(@RequestBody Department department) {
        return departmentService.add(department);
    }

    @GetMapping("/{id}")
    public Department getDepartmentById(@PathVariable int id) {
        return departmentService.getById(id);
    }

    @GetMapping
    public List<Department> getAllDepartments() {
        return departmentService.getAll();
    }

    @PutMapping("/{id}")
    public int updateDepartment(@PathVariable int id, @RequestBody Department department) {
        department.setDeptId(id);
        return departmentService.update(department);
    }

    @DeleteMapping("/{id}")
    public int deleteDepartment(@PathVariable int id) {
        return departmentService.deleteById(id);
    }
}