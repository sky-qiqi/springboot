package org.example.t3.service.impl;

import org.example.t3.entity.Course;
import org.example.t3.mapper.CourseMapper;
import org.example.t3.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    private CourseMapper courseMapper;

    @Override
    public int add(Course course) {
        return courseMapper.insert(course);
    }

    @Override
    public Course getById(int id) {
        return courseMapper.findById(id);
    }

    @Override
    public List<Course> getAll() {
        return courseMapper.findAll();
    }

    @Override
    public int update(Course course) {
        return courseMapper.update(course);
    }

    @Override
    public int deleteById(int id) {
        return courseMapper.deleteById(id);
    }
}