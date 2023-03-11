package com.food.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.food.entity.Setmeal;
import com.food.utils.Result;

public interface SetmealService extends IService<Setmeal> {
    Result<IPage<Setmeal>> getSetmealPageWithCondition(Long page, Long pageSize,String name);
}
