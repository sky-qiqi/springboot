package com.gzu.ssmdemo.service;


import com.gzu.ssmdemo.entity.User;

public interface UserService {

    User selectById(int id);

    int insert(User user);
}
