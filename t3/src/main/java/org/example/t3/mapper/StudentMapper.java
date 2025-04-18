package org.example.t3.mapper;

import org.apache.ibatis.annotations.*;
import org.example.t3.entity.Course;
import org.example.t3.entity.Student;
import org.example.t3.entity.StudentCourse;

import java.util.List;

@Mapper
public interface StudentMapper {
    @Insert("INSERT INTO students (student_name, enrollment_year) VALUES (#{studentName}, #{enrollmentYear})")
    @Options(useGeneratedKeys = true, keyProperty = "studentId")
    int insert(Student student);

    @Select("SELECT student_id, student_name, enrollment_year FROM students WHERE student_id = #{studentId}")
    @Results({
            @Result(property = "studentId", column = "student_id"),
            @Result(property = "studentName", column = "student_name"),
            @Result(property = "enrollmentYear", column = "enrollment_year"),
            @Result(property = "courses", column = "student_id",
                    many = @Many(select = "org.example.t3.mapper.CourseMapper.findCoursesByStudentId"))
    })
    Student findById(int studentId);

    @Select("SELECT student_id, student_name, enrollment_year FROM students")
    @Results({
            @Result(property = "studentId", column = "student_id"),
            @Result(property = "studentName", column = "student_name"),
            @Result(property = "enrollmentYear", column = "enrollment_year"),
            @Result(property = "courses", column = "student_id",
                    many = @Many(select = "org.example.t3.mapper.CourseMapper.findCoursesByStudentId"))
    })
    List<Student> findAll();

    @Update("UPDATE students SET student_name = #{studentName}, enrollment_year = #{enrollmentYear} WHERE student_id = #{studentId}")
    int update(Student student);

    @Delete("DELETE FROM students WHERE student_id = #{studentId}")
    int deleteById(int studentId);

    @Insert("INSERT INTO student_courses (student_id, course_id, enrollment_date) VALUES (#{studentId}, #{courseId}, #{enrollmentDate})")
    int enrollCourse(StudentCourse studentCourse);

    @Delete("DELETE FROM student_courses WHERE student_id = #{studentId} AND course_id = #{courseId}")
    int unenrollCourse(@Param("studentId") int studentId, @Param("courseId") int courseId);

    @Select("SELECT s.student_id, s.student_name, s.enrollment_year " +
            "FROM students s " +
            "JOIN student_courses sc ON s.student_id = sc.student_id " +
            "WHERE sc.course_id = #{courseId}")
    @Results({
            @Result(property = "studentId", column = "student_id"),
            @Result(property = "studentName", column = "student_name"),
            @Result(property = "enrollmentYear", column = "enrollment_year")
    })
    List<Student> findStudentsByCourseId(@Param("courseId") Integer courseId);

    @Delete("DELETE FROM student_courses WHERE course_id = #{courseId}")
    int deleteEnrollmentsByCourseId(@Param("courseId") int courseId);

    @Delete("DELETE FROM student_courses WHERE student_id = #{studentId}")
    int deleteEnrollmentsByStudentId(@Param("studentId") int studentId);
}