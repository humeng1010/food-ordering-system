package com.food.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.food.entity.ShoppingCart;
import com.food.mapper.ShoppingCartMapper;
import com.food.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

}
