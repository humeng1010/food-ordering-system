package com.food.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.food.entity.Setmeal;
import com.food.service.SetmealService;
import com.food.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @GetMapping("/page")
    public Result<IPage<Setmeal>> getSetmealPageWithCondition(@RequestParam Long page,@RequestParam Long pageSize,@RequestParam(name = "name",required = false) String name){

        return setmealService.getSetmealPageWithCondition(page,pageSize,name);
    }
}
