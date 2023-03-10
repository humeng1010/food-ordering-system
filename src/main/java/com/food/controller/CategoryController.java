package com.food.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.food.entity.Category;
import com.food.service.CategoryService;
import com.food.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;


@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/page")
    public Result<IPage<Category>> page(@RequestParam Long page, @RequestParam Long pageSize){
        return categoryService.getCategoryByPage(page,pageSize);
    }

    @PostMapping
    public Result<String> saveCategory(@RequestBody Category category){
        return categoryService.saveCategory(category);
    }

    @PutMapping
    public Result<String> updateCategoryById(@RequestBody Category category){
        return categoryService.updateCategoryById(category);
    }

    @DeleteMapping
    public Result<String> logicDeleteCategoryById(@RequestParam Long ids){

        return categoryService.deleteCategoryByIds(ids);
    }

}
