package org.example.t3.service.impl;

import org.example.t3.entity.User;
import org.example.t3.mapper.UserMapper;
import org.example.t3.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public int add(User user) {
        return userMapper.insert(user);
    }

    @Override
    public User getById(int id) {
        return userMapper.findById(id);
    }

    @Override
    public List<User> getAll() {
        return userMapper.findAll();
    }

    @Override
    public int update(User user) {
        return userMapper.update(user);
    }

    @Override
    public int deleteById(int id) {
        return userMapper.deleteById(id);
    }
}