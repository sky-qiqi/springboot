package com.example.work4.entity; // 包名已更新

import jakarta.persistence.*;
import java.time.Year;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer studentId;

    @Column(name = "student_name", nullable = false)
    private String studentName;

    @Column(name = "enrollment_year", nullable = false)
    private Year enrollmentYear;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<StudentCourse> studentCourses = new HashSet<>();

    // Getters and Setters
    public Integer getStudentId() { return studentId; }
    public void setStudentId(Integer studentId) { this.studentId = studentId; }
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public Year getEnrollmentYear() { return enrollmentYear; }
    public void setEnrollmentYear(Year enrollmentYear) { this.enrollmentYear = enrollmentYear; }
    public Set<StudentCourse> getStudentCourses() { return studentCourses; }
    public void setStudentCourses(Set<StudentCourse> studentCourses) { this.studentCourses = studentCourses; }

    // Helper methods
    public void addStudentCourse(StudentCourse studentCourse) {
        studentCourses.add(studentCourse);
        studentCourse.setStudent(this);
    }

    public void removeStudentCourse(StudentCourse studentCourse) {
        studentCourses.remove(studentCourse);
        studentCourse.setStudent(null);
    }
}