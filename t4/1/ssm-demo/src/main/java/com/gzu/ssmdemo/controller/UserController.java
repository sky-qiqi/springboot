package com.gzu.ssmdemo.controller;

import com.gzu.ssmdemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/user")
    public ModelAndView getUser(){
        System.out.println("getUser");
        ModelAndView mv = new ModelAndView();
        mv.addObject("msg",userService.selectById(9));
        mv.setViewName("hello");
        return mv;
    }

}