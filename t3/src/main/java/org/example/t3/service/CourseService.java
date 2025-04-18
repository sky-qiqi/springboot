package org.example.t3.service;

import org.example.t3.entity.Course;

import java.util.List;

public interface CourseService {
    int add(Course course);
    Course getById(int id);
    List<Course> getAll();
    int update(Course course);
    int deleteById(int id);
}