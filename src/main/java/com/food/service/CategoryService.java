package com.food.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.food.entity.Category;
import com.food.utils.Result;

import java.util.List;

public interface CategoryService extends IService<Category> {
    Result<IPage<Category>> getCategoryByPage(Long page, Long pageSize);

    Result<String> saveCategory(Category category);

    Result<String> updateCategoryById(Category category);

    Result<String> deleteCategoryByIds(Long ids);

    Result<List<Category>> getCategoryByType(Integer type);
}
