package org.example.t3.controller;

import org.example.t3.entity.User;
import org.example.t3.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public int addUser(@RequestBody User user) {
        return userService.add(user);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {
        return userService.getById(id);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAll();
    }

    @PutMapping("/{id}")
    public int updateUser(@PathVariable int id, @RequestBody User user) {
        user.setUserId(id);
        return userService.update(user);
    }

    @DeleteMapping("/{id}")
    public int deleteUser(@PathVariable int id) {
        return userService.deleteById(id);
    }
}