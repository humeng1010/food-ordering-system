package com.food.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.food.entity.Dish;
import com.food.mapper.DishMapper;
import com.food.service.DishService;
import org.springframework.stereotype.Service;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
}
