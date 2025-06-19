package com.example.work4.controller;

import com.example.work4.entity.Course;
import com.example.work4.entity.StudentCourse; // Need this to return list of enrollments
import com.example.work4.repository.CourseRepository;
import com.example.work4.repository.StudentCourseRepository; // Need this to get enrollments

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set; // Need Set

// Add @JsonIgnore to StudentCourse entity to break potential serialization loops
// (Or use DTOs, which is recommended for production)
// In StudentCourse.java:
// @JsonIgnore // Add this above private Student student;
// @JsonIgnore // Add this above private Course course;


@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentCourseRepository studentCourseRepository; // To get enrollments

    // --- Course CRUD ---

    // Create new course
    // POST http://localhost:8080/api/courses
    @PostMapping
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        // Ensure courseId is null for creation
        course.setCourseId(null);
        Course savedCourse = courseRepository.save(course);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCourse);
    }

    // Get all courses (includes their enrollments if fetched)
    // GET http://localhost:8080/api/courses
    @GetMapping
    public List<Course> getAllCourses() {
        // Accessing course.getStudentCourses() here might trigger lazy loading
        return courseRepository.findAll();
    }

    // Get course by ID (includes their enrollments if fetched)
    // GET http://localhost:8080/api/courses/{courseId}
    @GetMapping("/{courseId}")
    public ResponseEntity<Course> getCourseById(@PathVariable Integer courseId) {
        Optional<Course> course = courseRepository.findById(courseId);
        // Accessing course.getStudentCourses() here would load enrollments
        return course.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Update course details
    // PUT http://localhost:8080/api/courses/{courseId}
    @PutMapping("/{courseId}")
    public ResponseEntity<Course> updateCourse(@PathVariable Integer courseId, @RequestBody Course courseDetails) {
        Optional<Course> courseOptional = courseRepository.findById(courseId);

        if (courseOptional.isPresent()) {
            Course course = courseOptional.get();
            course.setCourseName(courseDetails.getCourseName());
            course.setCredit(courseDetails.getCredit());
            // Do not update studentCourses list from here

            Course updatedCourse = courseRepository.save(course);
            return ResponseEntity.ok(updatedCourse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete course by ID (cascades to StudentCourse due to orphanRemoval=true)
    // DELETE http://localhost:8080/api/courses/{courseId}
    @DeleteMapping("/{courseId}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Integer courseId) {
        if (courseRepository.existsById(courseId)) {
            courseRepository.deleteById(courseId); // Cascade deletes StudentCourse entities
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // --- Get Enrollments for a specific Course ---
    // This is part of showing the many-to-many relationship from the Course side

    // Get all enrollments (StudentCourse entities) for a specific course
    // GET http://localhost:8080/api/courses/{courseId}/students
    @GetMapping("/{courseId}/students")
    public ResponseEntity<Set<StudentCourse>> getCourseEnrollments(@PathVariable Integer courseId) {
        Optional<Course> courseOptional = courseRepository.findById(courseId);
        if (courseOptional.isPresent()) {
            // Accessing the collection triggers loading (if LAZY)
            Set<StudentCourse> enrollments = courseOptional.get().getStudentCourses();
            return ResponseEntity.ok(enrollments);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}