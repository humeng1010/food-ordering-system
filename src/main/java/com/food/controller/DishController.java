package com.food.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.food.entity.Dish;
import com.food.service.DishService;
import com.food.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
