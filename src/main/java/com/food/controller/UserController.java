package com.food.controller;

import com.food.dto.UserDto;
import com.food.entity.User;
import com.food.service.UserService;
import com.food.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/sendMsg")
    public Result<String> sendMsg(@RequestBody User user){
        return userService.sendMsg(user);
    }

    @PostMapping("/login")
    public Result<String> login(@RequestBody UserDto userDto, HttpSession session){
        return userService.login(userDto,session);
    }


}
