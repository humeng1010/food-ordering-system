package com.food.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.food.entity.Dish;
import com.food.utils.Result;

public interface DishService extends IService<Dish> {
    Result<IPage<Dish>> getDishByPageWithCondition(Long page, Long pageSize, String name);
}
