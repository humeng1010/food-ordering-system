package com.food.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.food.entity.Orders;
import com.food.mapper.OrdersMapper;
import com.food.service.OrdersService;
import org.springframework.stereotype.Service;

@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

}
