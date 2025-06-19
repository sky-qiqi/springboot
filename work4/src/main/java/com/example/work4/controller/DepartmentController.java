package com.example.work4.controller;

import com.example.work4.entity.Department;
import com.example.work4.entity.Employee; // 仍然需要导入 Employee，因为 createDepartment 和 addEmployeeToDepartment 方法中使用
import com.example.work4.repository.DepartmentRepository;
import com.example.work4.repository.EmployeeRepository; // 仍然需要导入 EmployeeRepository，因为 addEmployeeToDepartment 和 getEmployeesByDepartmentId 方法中使用
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/departments") // 这个控制器只处理 /api/departments 相关的请求
public class DepartmentController {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private EmployeeRepository employeeRepository; // 仍然需要注入，用于处理部门内的员工列表操作

    // --- Department CRUD ---

    // 创建新部门 (可选带员工列表)
    // POST http://localhost:8080/api/departments
    @PostMapping
    public ResponseEntity<Department> createDepartment(@RequestBody Department department) {
        if (department.getEmployees() != null) {
            department.getEmployees().forEach(employee -> {
                employee.setDepartment(department);
                if (employee.getEmpId() != null) {
                    employee.setEmpId(null);
                }
            });
        }
        Department savedDepartment = departmentRepository.save(department);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDepartment);
    }

    // 获取所有部门
    // GET http://localhost:8080/api/departments
    @GetMapping
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    // 根据部门 ID 获取部门
    // GET http://localhost:8080/api/departments/{deptId}
    @GetMapping("/{deptId}")
    public ResponseEntity<Department> getDepartmentById(@PathVariable Integer deptId) {
        Optional<Department> department = departmentRepository.findById(deptId);
        return department.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 更新部门基本信息
    // PUT http://localhost:8080/api/departments/{deptId}
    @PutMapping("/{deptId}")
    public ResponseEntity<Department> updateDepartment(@PathVariable Integer deptId, @RequestBody Department departmentDetails) {
        Optional<Department> departmentOptional = departmentRepository.findById(deptId);

        if (departmentOptional.isPresent()) {
            Department department = departmentOptional.get();
            department.setDeptName(departmentDetails.getDeptName());
            department.setLocation(departmentDetails.getLocation());

            Department updatedDepartment = departmentRepository.save(department);
            return ResponseEntity.ok(updatedDepartment);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 根据部门 ID 删除部门
    // DELETE http://localhost:8080/api/departments/{deptId}
    @DeleteMapping("/{deptId}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Integer deptId) {
        if (departmentRepository.existsById(deptId)) {
            departmentRepository.deleteById(deptId);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    // --- 与部门关联的员工操作 (仍在 DepartmentController 中) ---

    // 为指定部门添加新员工
    // POST http://localhost:8080/api/departments/{deptId}/employees
    @PostMapping("/{deptId}/employees")
    public ResponseEntity<Employee> addEmployeeToDepartment(@PathVariable Integer deptId, @RequestBody Employee employee) {
        Optional<Department> departmentOptional = departmentRepository.findById(deptId);

        if (departmentOptional.isPresent()) {
            Department department = departmentOptional.get();

            employee.setDepartment(department);
            department.addEmployee(employee);

            employee.setEmpId(null);
            Employee savedEmployee = employeeRepository.save(employee);

            return ResponseEntity.status(HttpStatus.CREATED).body(savedEmployee);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 获取指定部门的所有员工
    // GET http://localhost:8080/api/departments/{deptId}/employees
    @GetMapping("/{deptId}/employees")
    public ResponseEntity<List<Employee>> getEmployeesByDepartmentId(@PathVariable Integer deptId) {
        if (!departmentRepository.existsById(deptId)) {
            return ResponseEntity.notFound().build();
        }
        List<Employee> employees = employeeRepository.findByDepartment_DeptId(deptId);
        return ResponseEntity.ok(employees);
    }

    // 注意：直接操作单个员工的接口 (GET /api/employees/{empId}, PUT /api/employees/{empId}, DELETE /api/employees/{empId})
    // 已经从这里移除，应该放在 EmployeeController 中。
}