package org.example.t3.service;

import org.example.t3.entity.User;

import java.util.List;

public interface UserService {
    int add(User user);
    User getById(int id);
    List<User> getAll();
    int update(User user);
    int deleteById(int id);
}