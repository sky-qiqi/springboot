package org.example.t3.service;

import org.example.t3.entity.Student;
import org.example.t3.entity.StudentCourse;

import java.util.List;

public interface StudentService {
    int add(Student student);
    Student getById(int id);
    List<Student> getAll();
    int update(Student student);
    int deleteById(int id);
    int enrollCourse(StudentCourse studentCourse);
    int unenrollCourse(int studentId, int courseId);
    List<Student> getStudentsByCourseId(int courseId);
    int deleteEnrollmentsByCourseId(int courseId); // 添加了这一行
    int deleteEnrollmentsByStudentId(int studentId);
}