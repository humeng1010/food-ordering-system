package com.food.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.food.entity.Dish;
import com.food.mapper.DishMapper;
import com.food.service.DishService;
import com.food.utils.Result;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Override
    public Result<IPage<Dish>> getDishByPageWithCondition(Long page, Long pageSize, String name) {
        IPage<Dish> dishPage = new Page<>(page,pageSize);

        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper
                .like(StringUtils.hasText(name), Dish::getName,name)
                .orderByDesc(Dish::getUpdateTime);
        this.page(dishPage,dishLambdaQueryWrapper);

        return Result.success(dishPage);
    }
}
