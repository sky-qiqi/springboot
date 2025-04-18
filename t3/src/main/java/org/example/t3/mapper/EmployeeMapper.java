package org.example.t3.mapper;

import org.apache.ibatis.annotations.*;
import org.example.t3.entity.Employee;

import java.util.List;

@Mapper
public interface EmployeeMapper {
    @Insert("INSERT INTO employees (first_name, last_name, salary, dept_id) VALUES (#{firstName}, #{lastName}, #{salary}, #{deptId})")
    @Options(useGeneratedKeys = true, keyProperty = "empId")
    int insert(Employee employee);

    @Select("SELECT emp_id, first_name, last_name, salary, dept_id FROM employees WHERE emp_id = #{empId}")
    Employee findById(int empId);

    @Select("SELECT emp_id, first_name, last_name, salary, dept_id FROM employees WHERE dept_id = #{deptId}")
    @Results({
            @Result(property = "empId", column = "emp_id"),
            @Result(property = "firstName", column = "first_name"),
            @Result(property = "lastName", column = "last_name"),
            @Result(property = "salary", column = "salary"),
            @Result(property = "deptId", column = "dept_id")
    })
    List<Employee> findByDeptId(int deptId);

    @Select("SELECT emp_id, first_name, last_name, salary, dept_id FROM employees")
    List<Employee> findAll();

    @Update("UPDATE employees SET first_name = #{firstName}, last_name = #{lastName}, salary = #{salary}, dept_id = #{deptId} WHERE emp_id = #{empId}")
    int update(Employee employee);

    @Delete("DELETE FROM employees WHERE emp_id = #{empId}")
    int deleteById(int empId);
}