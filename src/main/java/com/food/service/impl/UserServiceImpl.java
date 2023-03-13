package com.food.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.food.dto.UserDto;
import com.food.entity.User;
import com.food.mapper.UserMapper;
import com.food.service.UserService;
import com.food.utils.Result;
import com.food.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpSession;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public Result<String> sendMsg(User user) {
        String phone = user.getPhone();
        Integer validateCode = ValidateCodeUtils.generateValidateCode(4);
        log.info("code={}",validateCode);

        ValueOperations<String, String> stringStringValueOperations = stringRedisTemplate.opsForValue();
        stringStringValueOperations.set(phone,validateCode.toString(),5, TimeUnit.MINUTES);

        return Result.success("验证码发送成功,将于5分钟之后过期");
    }

    @Override
    public Result<String> login(UserDto userDto, HttpSession session) {
        String phone = userDto.getPhone();
        String inputCode = userDto.getCode();
        ValueOperations<String, String> stringStringValueOperations = stringRedisTemplate.opsForValue();
        String code = stringStringValueOperations.get(phone);
        if (!Objects.equals(inputCode,code)){
            return Result.fail("验证码错误");
        }
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(StringUtils.hasText(userDto.getPhone()),User::getPhone,userDto.getPhone());
        User user = this.getOne(userLambdaQueryWrapper);
        if (!Objects.isNull(user)){
            session.setAttribute("user",user.getId());
            return Result.success("登陆成功");
        }
//        第一次注册
        User newUser = new User();
        newUser.setPhone(userDto.getPhone());
        String ramdomName = UUID.randomUUID().toString();
        newUser.setName(ramdomName);
        this.save(newUser);
        Long userId = newUser.getId();
        session.setAttribute("user",userId);
        return Result.success("注册成功,欢迎新用户(*^▽^*)");
    }
}
