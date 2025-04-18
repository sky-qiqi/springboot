package org.example.t3.mapper;

import org.apache.ibatis.annotations.*;
import org.example.t3.entity.Department;

import java.util.List;

@Mapper
public interface DepartmentMapper {
    @Insert("INSERT INTO departments (dept_name, location) VALUES (#{deptName}, #{location})")
    @Options(useGeneratedKeys = true, keyProperty = "deptId")
    int insert(Department department);

    @Select("SELECT dept_id, dept_name, location FROM departments WHERE dept_id = #{deptId}")
    @Results({
            @Result(property = "deptId", column = "dept_id"),
            @Result(property = "deptName", column = "dept_name"), // 添加这一行
            @Result(property = "location", column = "location"),   // 添加这一行
            @Result(property = "employees", column = "dept_id",
                    many = @Many(select = "org.example.t3.mapper.EmployeeMapper.findByDeptId"))
    })
    Department findById(int deptId);

    @Select("SELECT dept_id, dept_name, location FROM departments")
    List<Department> findAll();

    @Update("UPDATE departments SET dept_name = #{deptName}, location = #{location} WHERE dept_id = #{deptId}")
    int update(Department department);

    @Delete("DELETE FROM departments WHERE dept_id = #{deptId}")
    int deleteById(int deptId);
}