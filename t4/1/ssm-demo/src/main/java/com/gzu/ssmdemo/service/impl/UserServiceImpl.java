package com.gzu.ssmdemo.service.impl;

import com.gzu.ssmdemo.entity.User;
import com.gzu.ssmdemo.mapper.UserMapper;
import com.gzu.ssmdemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User selectById(int id) {
        return userMapper.selectById(id);
    }

    @Override
    public int insert(User user) {
        return userMapper.insert(user);
    }
}