package com.food.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.food.dto.SetmealDto;
import com.food.entity.Setmeal;
import com.food.utils.Result;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    Result<IPage<Setmeal>> getSetmealPageWithCondition(Long page, Long pageSize,String name);

    /**
     * 保存套餐
     * @param setmealDto 套餐dto
     * @return 成功信息
     */
    @Transactional
    Result<String> saveSetmeal(SetmealDto setmealDto);


    Result<SetmealDto> getSetmealAndDish(Long id);

    @Transactional
    Result<String> updateSetmealAndSetmealDish(SetmealDto setmealDto);

    @Transactional
    Result<String> logicDeleteSetmalAndSetmealDish(List<Long> ids);
}
