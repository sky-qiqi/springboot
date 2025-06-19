package com.example.work4.entity; // 包名已更新

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore; // <-- 导入 JsonIgnore 注解

@Entity
@Table(name = "student_courses")
public class StudentCourse {

    @EmbeddedId
    private StudentCourseId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("studentId")
    @JoinColumn(name = "student_id")
    @JsonIgnore // <-- 在 Student 字段上添加 JsonIgnore
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("courseId")
    @JoinColumn(name = "course_id")
    @JsonIgnore // <-- 在 Course 字段上添加 JsonIgnore
    private Course course;

    @Column(name = "enrollment_date", nullable = false)
    private LocalDate enrollmentDate;

    // Constructors
    public StudentCourse() {}

    public StudentCourse(Student student, Course course, LocalDate enrollmentDate) {
        this.student = student;
        this.course = course;
        this.enrollmentDate = enrollmentDate;
        if (student != null && student.getStudentId() != null && course != null && course.getCourseId() != null) {
            this.id = new StudentCourseId(student.getStudentId(), course.getCourseId());
        }
    }

    // Getters and Setters
    public StudentCourseId getId() { return id; }
    public void setId(StudentCourseId id) { this.id = id; }

    // 注意：通常我们仍然需要 getStudent() 和 getCourse() 方法以便在后端代码中使用这些关联
    // 但 Jackson 在序列化时会忽略它们因为 JsonIgnore
    public Student getStudent() { return student; }
    public void setStudent(Student student) {
        this.student = student;
        if (this.id == null) this.id = new StudentCourseId();
        if (student != null) this.id.setStudentId(student.getStudentId());
        else this.id.setStudentId(null);
    }
    public Course getCourse() { return course; }
    public void setCourse(Course course) {
        this.course = course;
        if (this.id == null) this.id = new StudentCourseId();
        if (course != null) this.id.setCourseId(course.getCourseId());
        else this.id.setCourseId(null);
    }
    public LocalDate getEnrollmentDate() { return enrollmentDate; }
    public void setEnrollmentDate(LocalDate enrollmentDate) { this.enrollmentDate = enrollmentDate; }

    // equals() and hashCode() based on id
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentCourse that = (StudentCourse) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}