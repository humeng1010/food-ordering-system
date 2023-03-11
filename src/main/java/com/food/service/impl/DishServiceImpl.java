package com.food.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.food.dto.DishDto;
import com.food.entity.Category;
import com.food.entity.Dish;
import com.food.entity.DishFlavor;
import com.food.mapper.DishMapper;
import com.food.service.CategoryService;
import com.food.service.DishFlavorService;
import com.food.service.DishService;
import com.food.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
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


    @Autowired
    private DishFlavorService dishFlavorService;
    @Override
    public Result<String> saveDish(DishDto dishDto) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDto,dish);
//        保存菜品
        this.save(dish);
//        获取菜品id
        Long dishId = dish.getId();

//        填充口味中的菜品id
        List<DishFlavor> dishFlavors = dishDto.getFlavors()
                .stream().peek(dishFlavor -> dishFlavor.setDishId(dishId))
                .collect(Collectors.toList());

//        批量保存
        dishFlavorService.saveBatch(dishFlavors);

        return Result.success("ok");
    }

    @Autowired
    private  CategoryService categoryService;


    @Override
    public Result<DishDto> getDishWithFlavorByDishId(Long id) {
//        查询dish
        Dish dish = this.getById(id);
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,id);
//        查询flavor
        List<DishFlavor> dishFlavors = dishFlavorService.list(queryWrapper);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);
        dishDto.setFlavors(dishFlavors);

//        查询分类名称
        Long categoryId = dish.getCategoryId();
        Category category = categoryService.getById(categoryId);

        dishDto.setCategoryName(category.getName());

        return Result.success(dishDto);
    }

    @Override
    public Result<String> updateDishOrFlavor(DishDto dishDto) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDto,dish);
//        更新dish
        this.updateById(dish);
//        更新flavor
//        先删除全部的flavor 再 批量保存
        LambdaUpdateWrapper<DishFlavor> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(dishDto.getId()!=null,DishFlavor::getDishId, dishDto.getId());
        dishFlavorService.remove(updateWrapper);

        //        填充口味中的菜品id
        List<DishFlavor> dishFlavors = dishDto.getFlavors()
                .stream().peek(dishFlavor -> dishFlavor.setDishId(dishDto.getId())).collect(Collectors.toList());

        dishFlavorService.saveBatch(dishFlavors);
        return Result.success("ok");
    }

    @Override
    public Result<String> deleteDishAndFlavorByDishIds(List<Long> ids) {
//        删除菜品
        this.removeByIds(ids);
//        删除菜品对应的口味
        LambdaUpdateWrapper<DishFlavor> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(DishFlavor::getDishId,ids);
        dishFlavorService.remove(updateWrapper);
        return Result.success("ok");
    }

    @Override
    public Result<String> updateStatusDishByIds(List<Long> ids, Integer status) {
        LambdaUpdateWrapper<Dish> updateWrapper = new LambdaUpdateWrapper<>();

        updateWrapper.set(Dish::getStatus,status).in(Dish::getId,ids);
        this.update(updateWrapper);
        return Result.success("ok");
    }

}
