package com.food.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.food.dto.SetmealDto;
import com.food.entity.Setmeal;
import com.food.service.SetmealService;
import com.food.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @GetMapping("/page")
    public Result<IPage<Setmeal>> getSetmealPageWithCondition(@RequestParam Long page,@RequestParam Long pageSize,@RequestParam(name = "name",required = false) String name){

        return setmealService.getSetmealPageWithCondition(page,pageSize,name);
    }

    @PostMapping
    public Result<String> saveSetmeal(@RequestBody SetmealDto setmealDto){
        return setmealService.saveSetmeal(setmealDto);

    }

    @GetMapping("/{id}")
    public Result<SetmealDto> getSetmealAndDish(@PathVariable Long id){
        return setmealService.getSetmealAndDish(id);
    }

    @PutMapping
    public Result<String> updateSetmealAndSetmealDish(@RequestBody SetmealDto setmealDto){
        return setmealService.updateSetmealAndSetmealDish(setmealDto);
    }

    @DeleteMapping
    public Result<String> logicDeleteSetmalAndSetmealDish(@RequestParam List<Long> ids){
        return setmealService.logicDeleteSetmalAndSetmealDish(ids);
    }


    @PostMapping("/status/{status}")
    public Result<String> updateStatusByIds(@PathVariable Integer status,@RequestParam List<Long> ids){
        return setmealService.updateStatusByIds(status,ids);

    }

    @GetMapping("/list")
    public Result<List<Setmeal>> getSetmealByCategoryId(@RequestParam Long categoryId,@RequestParam Integer status){
        return setmealService.getSetmealByCategoryId(categoryId,status);
    }

}
