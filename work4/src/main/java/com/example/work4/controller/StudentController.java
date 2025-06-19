package com.example.work4.controller;

import com.example.work4.entity.Student;
import com.example.work4.entity.StudentCourse; // Need this to return list of enrollments
import com.example.work4.repository.StudentRepository;
import com.example.work4.repository.StudentCourseRepository; // Need this to get enrollments

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Year; // Need Year for entity
import java.util.List;
import java.util.Optional;
import java.util.Set; // Need Set

// Add @JsonIgnore to StudentCourse entity to break potential serialization loops
// (Or use DTOs, which is recommended for production)
// In StudentCourse.java:
// @JsonIgnore // Add this above private Student student;
// @JsonIgnore // Add this above private Course course;
// Make sure you have Jackson annotations dependency if not already included
// <dependency> <groupId>com.fasterxml.jackson.core</groupId> <artifactId>jackson-annotations</artifactId> </dependency>


@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentCourseRepository studentCourseRepository; // To get enrollments

    // --- Student CRUD ---

    // Create new student
    // POST http://localhost:8080/api/students
    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        // Ensure studentId is null for creation
        student.setStudentId(null);
        Student savedStudent = studentRepository.save(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedStudent);
    }

    // Get all students (includes their enrollments if fetched)
    // GET http://localhost:8080/api/students
    @GetMapping
    public List<Student> getAllStudents() {
        // When fetching all students, accessing student.getStudentCourses() might trigger lazy loading
        // and potentially the JSON recursion issue if not handled by @JsonIgnore or DTOs.
        return studentRepository.findAll();
    }

    // Get student by ID (includes their enrollments if fetched)
    // GET http://localhost:8080/api/students/{studentId}
    @GetMapping("/{studentId}")
    public ResponseEntity<Student> getStudentById(@PathVariable Integer studentId) {
        Optional<Student> student = studentRepository.findById(studentId);
        // Accessing student.getStudentCourses() here would load enrollments
        return student.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Update student details
    // PUT http://localhost:8080/api/students/{studentId}
    @PutMapping("/{studentId}")
    public ResponseEntity<Student> updateStudent(@PathVariable Integer studentId, @RequestBody Student studentDetails) {
        Optional<Student> studentOptional = studentRepository.findById(studentId);

        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();
            student.setStudentName(studentDetails.getStudentName());
            student.setEnrollmentYear(studentDetails.getEnrollmentYear());
            // Do not update studentCourses list from here

            Student updatedStudent = studentRepository.save(student);
            return ResponseEntity.ok(updatedStudent);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete student by ID (cascades to StudentCourse due to orphanRemoval=true)
    // DELETE http://localhost:8080/api/students/{studentId}
    @DeleteMapping("/{studentId}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Integer studentId) {
        if (studentRepository.existsById(studentId)) {
            studentRepository.deleteById(studentId); // Cascade deletes StudentCourse entities
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // --- Get Enrollments for a specific Student ---
    // This is part of showing the many-to-many relationship from the Student side

    // Get all enrollments (StudentCourse entities) for a specific student
    // GET http://localhost:8080/api/students/{studentId}/courses
    @GetMapping("/{studentId}/courses")
    public ResponseEntity<Set<StudentCourse>> getStudentEnrollments(@PathVariable Integer studentId) {
        Optional<Student> studentOptional = studentRepository.findById(studentId);
        if (studentOptional.isPresent()) {
            // Accessing the collection triggers loading (if LAZY)
            Set<StudentCourse> enrollments = studentOptional.get().getStudentCourses();
            return ResponseEntity.ok(enrollments);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}