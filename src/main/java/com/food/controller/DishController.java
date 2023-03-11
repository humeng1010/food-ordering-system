package com.food.controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.food.dto.DishDto;
import com.food.entity.Dish;
import com.food.service.DishService;
import com.food.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;

    /**
     * 分页条件查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public Result<IPage<Dish>> getDishByPageWithCondition(
            @RequestParam("page") Long page,
            @RequestParam("pageSize") Long pageSize,
            @RequestParam(name = "name",required = false)String name){

        return dishService.getDishByPageWithCondition(page,pageSize,name);
    }

    @PostMapping
    public Result<String> saveDish(@RequestBody DishDto dishDto){
        return dishService.saveDish(dishDto);
    }

    @GetMapping("/{id}")
    public Result<DishDto> getDishWithFlavorByDishId(@PathVariable Long id){
        return dishService.getDishWithFlavorByDishId(id);
    }

    @PutMapping
    public Result<String> updateDishOrFlavor(@RequestBody DishDto dishDto){
        return dishService.updateDishOrFlavor(dishDto);

    }

    @DeleteMapping
    public Result<String> deleteByIds(@RequestParam List<Long> ids){
        return dishService.deleteDishAndFlavorByDishIds(ids);
    }

    @PostMapping("/status/{status}")
    public Result<String> updateDishByIds(@RequestParam List<Long> ids,@PathVariable Integer status){

        return dishService.updateStatusDishByIds(ids,status);
    }
}
