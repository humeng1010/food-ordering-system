package com.food.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.food.dto.DishDto;
import com.food.entity.Dish;
import com.food.utils.Result;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DishService extends IService<Dish> {
    Result<IPage<Dish>> getDishByPageWithCondition(Long page, Long pageSize, String name);

    @Transactional
    Result<String> saveDish(DishDto dishDto);

    Result<DishDto> getDishWithFlavorByDishId(Long id);

    @Transactional
    Result<String> updateDishOrFlavor(DishDto dishDto);

    @Transactional
    Result<String> deleteDishAndFlavorByDishIds(List<Long> ids);

    Result<String> updateStatusDishByIds(List<Long> ids, Integer status);

    Result<List<Dish>> getDshList(Long categoryId);
}
