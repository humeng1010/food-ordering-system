package com.food.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.food.entity.Setmeal;
import com.food.mapper.SetmealMapper;
import com.food.service.SetmealService;
import org.springframework.stereotype.Service;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

}
