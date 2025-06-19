package com.example.work4.entity; // 包名已更新

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer courseId;

    @Column(name = "course_name", nullable = false)
    private String courseName;

    @Column(nullable = false)
    private Integer credit;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<StudentCourse> studentCourses = new HashSet<>();

    // Getters and Setters
    public Integer getCourseId() { return courseId; }
    public void setCourseId(Integer courseId) { this.courseId = courseId; }
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    public Integer getCredit() { return credit; }
    public void setCredit(Integer credit) { this.credit = credit; }
    public Set<StudentCourse> getStudentCourses() { return studentCourses; }
    public void setStudentCourses(Set<StudentCourse> studentCourses) { this.studentCourses = studentCourses; }

    // Helper methods
    public void addStudentCourse(StudentCourse studentCourse) {
        studentCourses.add(studentCourse);
        studentCourse.setCourse(this);
    }

    public void removeStudentCourse(StudentCourse studentCourse) {
        studentCourses.remove(studentCourse);
        studentCourse.setCourse(null);
    }
}