package org.example.t3.controller;

import org.example.t3.entity.Course;
import org.example.t3.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @PostMapping
    public int addCourse(@RequestBody Course course) {
        return courseService.add(course);
    }

    @GetMapping("/{id}")
    public Course getCourseById(@PathVariable int id) {
        return courseService.getById(id);
    }

    @GetMapping
    public List<Course> getAllCourses() {
        return courseService.getAll();
    }

    @PutMapping("/{id}")
    public int updateCourse(@PathVariable int id, @RequestBody Course course) {
        course.setCourseId(id);
        return courseService.update(course);
    }

    @DeleteMapping("/{id}")
    public int deleteCourse(@PathVariable int id) {
        return courseService.deleteById(id);
    }
}