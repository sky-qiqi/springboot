package com.example.work4.controller; // 包名

import com.example.work4.entity.Department; // 导入 Department 实体
import com.example.work4.entity.Employee; // 导入 Employee 实体
import com.example.work4.repository.DepartmentRepository; // 导入 DepartmentRepository
import com.example.work4.repository.EmployeeRepository; // 导入 EmployeeRepository
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus; // 导入 HttpStatus
import org.springframework.http.ResponseEntity; // 导入 ResponseEntity
import org.springframework.web.bind.annotation.*; // 导入所有 Web 注解

import java.util.List; // 导入 List
import java.util.Optional; // 导入 Optional

@RestController // 标记这是一个 REST 控制器，自动将返回对象序列化为 JSON/XML
@RequestMapping("/api/employees") // 设置这个控制器处理的基础 URL 路径为 /api/employees
public class EmployeeController {

    @Autowired // 注入 EmployeeRepository，用于对 Employee 实体进行数据库操作
    private EmployeeRepository employeeRepository;

    @Autowired // 注入 DepartmentRepository，用于在处理员工时查找和关联 Department
    private DepartmentRepository departmentRepository;

    // --- Employee CRUD Operations ---

    // 获取所有员工
    // GET http://localhost:8080/api/employees
    @GetMapping // 映射 GET 请求到 /api/employees
    public List<Employee> getAllEmployees() {
        // 使用 findAll() 方法获取所有 Employee 实体列表
        return employeeRepository.findAll();
    }

    // 根据 ID 获取特定员工
    // GET http://localhost:8080/api/employees/{empId}
    @GetMapping("/{empId}") // 映射 GET 请求到 /api/employees/{empId}，{empId} 是路径变量
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Integer empId) {
        // 使用 findById() 方法根据主键查找 Employee 实体
        Optional<Employee> employee = employeeRepository.findById(empId);
        // 如果找到 Employee，返回 200 OK 和 Employee 对象
        // 如果未找到，返回 404 Not Found
        return employee.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 创建新员工
    // POST http://localhost:8080/api/employees
    // 请求体中可以包含员工信息，包括通过 department 字段关联现有部门 (只需提供部门 ID)
    @PostMapping // 映射 POST 请求到 /api/employees
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        // 处理请求体中可能包含的部门关联信息
        if (employee.getDepartment() != null && employee.getDepartment().getDeptId() != null) {
            Integer deptId = employee.getDepartment().getDeptId();
            Optional<Department> departmentOptional = departmentRepository.findById(deptId);

            if (departmentOptional.isPresent()) {
                Department department = departmentOptional.get();
                employee.setDepartment(department); // 设置员工的部门关联
                department.addEmployee(employee); // 将员工添加到部门的员工集合中 (维护双向关系)
            } else {
                // 如果请求体中指定的部门 ID 不存在，返回 400 Bad Request
                return ResponseEntity.badRequest().body(null);
            }
        } else {
            // 如果请求体中没有指定部门，或 department 对象/deptId 为 null，则将员工的部门设为 null
            employee.setDepartment(null);
        }

        // 确保在创建新员工时，其 ID 由数据库自动生成
        employee.setEmpId(null);
        // 保存 Employee 实体，JPA 会处理部门外键的设置
        Employee savedEmployee = employeeRepository.save(employee);
        // 返回 201 Created 状态码和保存成功的 Employee 对象
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEmployee);
    }


    // 更新特定员工信息 (包括更改部门)
    // PUT http://localhost:8080/api/employees/{empId}
    @PutMapping("/{empId}") // 映射 PUT 请求到 /api/employees/{empId}
    public ResponseEntity<Employee> updateEmployee(@PathVariable Integer empId, @RequestBody Employee employeeDetails) {
        // 查找要更新的现有员工
        Optional<Employee> employeeOptional = employeeRepository.findById(empId);

        if (employeeOptional.isPresent()) {
            Employee employee = employeeOptional.get(); // 获取现有员工实体

            // 更新员工的基本字段
            employee.setFirstName(employeeDetails.getFirstName());
            employee.setLastName(employeeDetails.getLastName());
            employee.setSalary(employeeDetails.getSalary());

            // --- 处理部门关联的更新 ---
            // 检查请求体中是否提供了部门信息
            if (employeeDetails.getDepartment() != null) {
                Integer newDeptId = employeeDetails.getDepartment().getDeptId();
                if (newDeptId != null) {
                    // 请求体中指定了新的部门 ID
                    Optional<Department> newDepartmentOptional = departmentRepository.findById(newDeptId);
                    if (newDepartmentOptional.isPresent()) {
                        Department oldDepartment = employee.getDepartment(); // 获取员工当前的部门
                        Department newDepartment = newDepartmentOptional.get(); // 获取新的部门实体

                        // 如果员工当前部门和请求指定的新部门不同 (或当前没有部门)
                        if (oldDepartment == null || !oldDepartment.getDeptId().equals(newDeptId)) {
                            // 1. 从旧部门的员工集合中移除该员工 (如果原来有部门的话)
                            if (oldDepartment != null) {
                                oldDepartment.removeEmployee(employee); // 使用 Department 实体中的辅助方法
                                // 注意: 这里是否需要保存 oldDepartment 取决于 JPA 的 FetchType 和 Cascade 设置。
                                // 对于 LAZY + no cascade on Department side, 通常不需要在这里保存 oldDepartment。
                                // 保存 employee 会更新其外键，JPA 会在加载 Department 时反映变化。
                            }

                            // 2. 设置员工的新部门关联
                            employee.setDepartment(newDepartment);
                            // 3. 将员工添加到新部门的员工集合中
                            newDepartment.addEmployee(employee); // 使用 Department 实体中的辅助方法
                            // 注意: 同理，这里是否需要保存 newDepartment 也取决于 FetchType/Cascade。
                        }
                        // 如果新部门 ID 与当前部门 ID 相同，则不做任何改变

                    } else {
                        // 请求体中指定的部门 ID 不存在
                        return ResponseEntity.badRequest().body(null); // 返回 400 Bad Request
                    }
                } else {
                    // 请求体中 department 对象不为 null，但 deptId 为 null。
                    // 这可能表示客户端意图将部门设为 null。
                    // 检查当前员工是否有部门，如果有，则移除关联。
                    if (employee.getDepartment() != null) {
                        Department oldDepartment = employee.getDepartment();
                        oldDepartment.removeEmployee(employee);
                        employee.setDepartment(null); // 将员工部门设为 null
                        // departmentRepository.save(oldDepartment); // 根据情况决定是否需要保存旧部门
                    }
                }
            } else {
                // 请求体中 department 字段为 null。
                // 检查当前员工是否有部门，如果有，则移除关联。
                if (employee.getDepartment() != null) {
                    Department oldDepartment = employee.getDepartment();
                    oldDepartment.removeEmployee(employee);
                    employee.setDepartment(null); // 将员工部门设为 null
                    // departmentRepository.save(oldDepartment); // 根据情况决定是否需要保存旧部门
                }
            }

            // 保存更新后的 Employee 实体，这会更新其 dept_id 外键
            Employee updatedEmployee = employeeRepository.save(employee);
            // 返回 200 OK 和更新后的 Employee 对象
            return ResponseEntity.ok(updatedEmployee);

        } else {
            // 未找到要更新的员工
            return ResponseEntity.notFound().build(); // 返回 404 Not Found
        }
    }


    // 根据 ID 删除特定员工
    // DELETE http://localhost:8080/api/employees/{empId}
    @DeleteMapping("/{empId}") // 映射 DELETE 请求到 /api/employees/{empId}
    public ResponseEntity<Void> deleteEmployee(@PathVariable Integer empId) {
        // 查找要删除的员工
        Optional<Employee> employeeOptional = employeeRepository.findById(empId);

        if (employeeOptional.isPresent()) {
            Employee employee = employeeOptional.get(); // 获取员工实体

            // 在删除员工实体前，从其所属部门的员工集合中移除它
            // 这有助于维护 Department 实体端的集合状态，并避免潜在的 LazyInitializationException
            if (employee.getDepartment() != null) {
                employee.getDepartment().removeEmployee(employee); // 使用 Department 实体中的辅助方法
                // 注意: 这里通常不需要显式保存 Department，删除 Employee 会自动更新其外键，
                // JPA 会在加载 Department 集合时处理。
                // departmentRepository.save(employee.getDepartment()); // 根据情况决定是否需要
            }

            // 删除 Employee 实体
            employeeRepository.delete(employee);
            // 返回 204 No Content 状态码，表示成功但没有返回体
            return ResponseEntity.noContent().build();
        } else {
            // 未找到要删除的员工
            return ResponseEntity.notFound().build(); // 返回 404 Not Found
        }
    }
}