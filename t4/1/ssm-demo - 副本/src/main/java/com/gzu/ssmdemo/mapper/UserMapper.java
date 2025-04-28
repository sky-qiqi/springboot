package com.gzu.ssmdemo.mapper;


import com.gzu.ssmdemo.entity.User;

public interface UserMapper {

    User selectById(int id);

    int insert(User user);
}

