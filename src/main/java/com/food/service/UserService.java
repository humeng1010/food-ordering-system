package com.food.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.food.dto.UserDto;
import com.food.entity.User;
import com.food.utils.Result;

import javax.servlet.http.HttpSession;

public interface UserService extends IService<User> {
    Result<String> sendMsg(User user);

    Result<String> login(UserDto userDto, HttpSession session);
}
