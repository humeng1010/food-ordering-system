package com.food.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.food.entity.Category;
import com.food.entity.Dish;
import com.food.entity.Setmeal;
import com.food.exception.DuplicateException;
import com.food.mapper.CategoryMapper;
import com.food.service.CategoryService;
import com.food.service.DishService;
import com.food.service.SetmealService;
import com.food.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Override
    public Result<IPage<Category>> getCategoryByPage(Long page, Long pageSize) {
        IPage<Category> categoryPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        this.page(categoryPage,queryWrapper);
        return Result.success(categoryPage);
    }

    @Override
    public Result<String> saveCategory(Category category) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(category.getName()),Category::getName,category.getName());
        Category one = this.getOne(queryWrapper);
        if (!Objects.isNull(one)){
            throw new DuplicateException("名称重复");
        }
        boolean isSave = this.save(category);
        if (isSave){
            return Result.success("ok");
        }
        return Result.fail("添加失败");
    }

    @Override
    public Result<String> updateCategoryById(Category category) {
        boolean res = this.updateById(category);
        if (res){
            return Result.success("ok");
        }
        return Result.fail("修改失败");
    }

    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;
    @Override
    public Result<String> deleteCategoryByIds(Long ids) {
//        判断分类中是否关联了菜品
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,ids);
        List<Dish> dishList = dishService.list(dishLambdaQueryWrapper);
        if (!dishList.isEmpty()){
            return Result.fail("请先删除该分类中的菜品");
        }
//        判断分类中是否关联了套餐
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,ids);
        List<Setmeal> setmealList = setmealService.list(setmealLambdaQueryWrapper);
        if (!setmealList.isEmpty()){
            return Result.fail("请先删除该分类中的套餐");
        }

        this.removeById(ids);
        return Result.success("ok");
    }

    @Override
    public Result<List<Category>> getCategoryByType(Integer type) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(type!=null,Category::getType,type)
                .orderByAsc(Category::getSort)
                .orderByDesc(Category::getUpdateTime);
        List<Category> categoryList = this.list(queryWrapper);
        return Result.success(categoryList);
    }
}
