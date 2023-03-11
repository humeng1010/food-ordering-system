package com.food.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.food.entity.Setmeal;
import com.food.mapper.SetmealMapper;
import com.food.service.SetmealService;
import com.food.utils.Result;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Override
    public Result<IPage<Setmeal>> getSetmealPageWithCondition(Long page, Long pageSize,String name) {
        IPage<Setmeal> setmealPage = new Page<>(page,pageSize);
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(name),Setmeal::getName,name);
        this.page(setmealPage,queryWrapper);
        return Result.success(setmealPage);
    }
}
