package com.food.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.food.entity.ShoppingCart;
import com.food.service.ShoppingCartService;
import com.food.utils.Result;
import com.food.utils.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
@Slf4j
public class ShoppingCartController {
    @Autowired
    ShoppingCartService shoppingCartService;

    @GetMapping("/list")
    public Result<List<ShoppingCart>> getCurrentUserShoppingCart(){
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId,ThreadLocalUtil.get("user"));
        List<ShoppingCart> shoppingCartList = shoppingCartService.list(shoppingCartLambdaQueryWrapper);
        return Result.success(shoppingCartList);
    }

    @PostMapping("/add")
    public Result<ShoppingCart> addShop2Cart(@RequestBody ShoppingCart shoppingCart){
        shoppingCart.setUserId((Long) ThreadLocalUtil.get("user"));
        shoppingCartService.save(shoppingCart);
        return Result.success(shoppingCart);
    }

    @PostMapping("/sub")
    public Result<String> subShoppingCart(@RequestBody ShoppingCart shoppingCart){
        log.info("dishId:{}",shoppingCart.getDishId());

        Long dishId = shoppingCart.getDishId();
        Long userId = (Long) ThreadLocalUtil.get("user");
        LambdaUpdateWrapper<ShoppingCart> shoppingCartLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        shoppingCartLambdaUpdateWrapper.eq(ShoppingCart::getDishId,dishId).eq(ShoppingCart::getUserId,userId);
        shoppingCartService.remove(shoppingCartLambdaUpdateWrapper);

        return Result.success("ok");
    }

    @DeleteMapping("/clean")
    public Result<String> cleanShoppingCart(){
        LambdaUpdateWrapper<ShoppingCart> shoppingCartLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        shoppingCartLambdaUpdateWrapper.in(ShoppingCart::getUserId,ThreadLocalUtil.get("user"));
        shoppingCartService.remove(shoppingCartLambdaUpdateWrapper);
        return Result.success("ok");
    }
}
