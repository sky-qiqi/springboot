package org.example.t3.service.impl;

import org.example.t3.entity.Student;
import org.example.t3.entity.StudentCourse;
import org.example.t3.mapper.StudentMapper;
import org.example.t3.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentMapper studentMapper;

    @Override
    public int add(Student student) {
        return studentMapper.insert(student);
    }

    @Override
    public Student getById(int id) {
        return studentMapper.findById(id);
    }

    @Override
    public List<Student> getAll() {
        return studentMapper.findAll();
    }

    @Override
    public int update(Student student) {
        return studentMapper.update(student);
    }

    @Override
    public int deleteById(int id) {
        return studentMapper.deleteById(id);
    }

    @Override
    public int enrollCourse(StudentCourse studentCourse) {
        return studentMapper.enrollCourse(studentCourse);
    }

    @Override
    public int unenrollCourse(int studentId, int courseId) {
        return studentMapper.unenrollCourse(studentId, courseId);
    }

    @Override
    public List<Student> getStudentsByCourseId(int courseId) {
        return studentMapper.findStudentsByCourseId(courseId);
    }

    @Override
    public int deleteEnrollmentsByCourseId(int courseId) { // 实现了这个方法
        return studentMapper.deleteEnrollmentsByCourseId(courseId);
    }
    @Override
    public int deleteEnrollmentsByStudentId(int studentId) {
        return studentMapper.deleteEnrollmentsByStudentId(studentId);
    }
}