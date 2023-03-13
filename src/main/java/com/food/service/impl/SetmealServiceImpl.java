package com.food.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.food.dto.SetmealDto;
import com.food.entity.Setmeal;
import com.food.entity.SetmealDish;
import com.food.exception.DuplicateException;
import com.food.mapper.SetmealDishMapper;
import com.food.mapper.SetmealMapper;
import com.food.service.SetmealService;
import com.food.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Override
    public Result<IPage<Setmeal>> getSetmealPageWithCondition(Long page, Long pageSize,String name) {
        IPage<Setmeal> setmealPage = new Page<>(page,pageSize);
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(name),Setmeal::getName,name)
                .orderByDesc(Setmeal::getUpdateTime);
        this.page(setmealPage,queryWrapper);
        return Result.success(setmealPage);
    }


    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Override
    public Result<String> saveSetmeal(SetmealDto setmealDto) {
        isSetmealNameRepeat(setmealDto);

        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDto,setmeal);
        this.save(setmeal);

        Long setmealId = setmeal.getId();
//        setmealId=null
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes = setmealDishes.stream().peek(setmealDish -> setmealDish.setSetmealId(setmealId)).collect(Collectors.toList());

        setmealDishes.forEach(setmealDish -> setmealDishMapper.insert(setmealDish));

        return Result.success("ok");
    }

    /**
     * 检查套餐名是否重复
     * @param setmealDto 接收到的数据
     */
    private void isSetmealNameRepeat(SetmealDto setmealDto) {
        String name = setmealDto.getName();
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getName,name);
        int count = this.count(setmealLambdaQueryWrapper);
        if (count>0){
            throw new DuplicateException("名称重复,换个名吧");
        }
    }

    @Override
    public Result<SetmealDto> getSetmealAndDish(Long id) {
        Setmeal setmeal = this.getById(id);

        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,id);
        List<SetmealDish> setmealDishes = setmealDishMapper.selectList(queryWrapper);
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal,setmealDto);

        setmealDto.setSetmealDishes(setmealDishes);

        return Result.success(setmealDto);
    }

    @Override
    public Result<String> updateSetmealAndSetmealDish(SetmealDto setmealDto) {

        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDto,setmeal);
        this.updateById(setmeal);

        Long setmealId = setmeal.getId();
        LambdaUpdateWrapper<SetmealDish> setmealDishLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        setmealDishLambdaUpdateWrapper.eq(SetmealDish::getSetmealId,setmealId);
//        TODO 优化:使用ServiceImpl的批量删除
        setmealDishMapper.delete(setmealDishLambdaUpdateWrapper);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes = setmealDishes.stream().peek(setmealDish -> setmealDish.setSetmealId(setmealId)).collect(Collectors.toList());
        setmealDishes.forEach(setmealDish -> setmealDishMapper.insert(setmealDish));


        return Result.success("ok");
    }

    @Override
    public Result<String> logicDeleteSetmalAndSetmealDish(List<Long> ids) {
//      删除套餐
        this.removeByIds(ids);
//        删除套餐中的菜品
//          delete from setmeal_dish where setmeal_id in (?);
        LambdaUpdateWrapper<SetmealDish> setmealDishLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        setmealDishLambdaUpdateWrapper.in(SetmealDish::getSetmealId,ids);
        setmealDishMapper.delete(setmealDishLambdaUpdateWrapper);

        return Result.success("ok");
    }

    @Override
    public Result<String> updateStatusByIds(Integer status, List<Long> ids) {
        LambdaUpdateWrapper<Setmeal> setmealLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        setmealLambdaUpdateWrapper.set(Setmeal::getStatus,status).in(Setmeal::getId,ids);
        this.update(setmealLambdaUpdateWrapper);
        return Result.success("ok");
    }
}
