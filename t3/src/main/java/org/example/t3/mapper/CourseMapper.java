package org.example.t3.mapper;

import org.apache.ibatis.annotations.*;
import org.example.t3.entity.Course;
import org.example.t3.entity.Student;

import java.util.List;

@Mapper
public interface CourseMapper {
    @Insert("INSERT INTO courses (course_name, credit) VALUES (#{courseName}, #{credit})")
    @Options(useGeneratedKeys = true, keyProperty = "courseId")
    int insert(Course course);

    @Select("SELECT course_id, course_name, credit FROM courses WHERE course_id = #{courseId}")
    @Results({
            @Result(property = "courseId", column = "course_id"),
            @Result(property = "courseName", column = "course_name"),
            @Result(property = "credit", column = "credit"),
            @Result(property = "students", column = "course_id",
                    many = @Many(select = "org.example.t3.mapper.StudentMapper.findStudentsByCourseId"))
    })
    Course findById(int courseId);

    @Select("SELECT course_id, course_name, credit FROM courses")
    List<Course> findAll();

    @Update("UPDATE courses SET course_name = #{courseName}, credit = #{credit} WHERE course_id = #{courseId}")
    int update(Course course);

    @Delete("DELETE FROM courses WHERE course_id = #{courseId}")
    int deleteById(int courseId);

    @Select("SELECT c.course_id, c.course_name, c.credit FROM courses c " +
            "JOIN student_courses sc ON c.course_id = sc.course_id " +
            "WHERE sc.student_id = #{studentId}")
    @Results({
            @Result(property = "courseId", column = "course_id"),
            @Result(property = "courseName", column = "course_name"),
            @Result(property = "credit", column = "credit")
    })
    List<Course> findCoursesByStudentId(int studentId);

    @Select("SELECT s.student_id, s.student_name, s.enrollment_year FROM students s " +
            "JOIN student_courses sc ON s.student_id = sc.student_id " +
            "WHERE sc.course_id = #{courseId}")
    List<Student> findStudentsByCourseId(int courseId);
}