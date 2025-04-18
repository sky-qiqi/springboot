package org.example.t3.controller;

import org.example.t3.entity.Student;
import org.example.t3.entity.StudentCourse;
import org.example.t3.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @PostMapping
    public int addStudent(@RequestBody Student student) {
        return studentService.add(student);
    }

    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable int id) {
        return studentService.getById(id);
    }

    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.getAll();
    }

    @PutMapping("/{id}")
    public int updateStudent(@PathVariable int id, @RequestBody Student student) {
        student.setStudentId(id);
        return studentService.update(student);
    }

    @DeleteMapping("/{id}")
    public int deleteStudent(@PathVariable int id) {
        return studentService.deleteById(id);
    }

    @PostMapping("/{studentId}/enroll/{courseId}")
    public int enrollCourse(@PathVariable int studentId, @PathVariable int courseId) {
        StudentCourse studentCourse = new StudentCourse();
        studentCourse.setStudentId(studentId);
        studentCourse.setCourseId(courseId);
        studentCourse.setEnrollmentDate(new Date());
        return studentService.enrollCourse(studentCourse);
    }

    @DeleteMapping("/{studentId}/unenroll/{courseId}")
    public int unenrollCourse(@PathVariable int studentId, @PathVariable int courseId) {
        return studentService.unenrollCourse(studentId, courseId);
    }

    @GetMapping("/course/{courseId}")
    public List<Student> getStudentsByCourseId(@PathVariable int courseId) {
        return studentService.getStudentsByCourseId(courseId);
    }

    @DeleteMapping("/courses/{courseId}/enrollments") // 添加了这个接口
    public int deleteEnrollmentsByCourseId(@PathVariable int courseId) {
        return studentService.deleteEnrollmentsByCourseId(courseId);
    }
    @DeleteMapping("/{studentId}/enrollments")
    public int deleteEnrollmentsByStudentId(@PathVariable int studentId) {
        return studentService.deleteEnrollmentsByStudentId(studentId);
    }
}