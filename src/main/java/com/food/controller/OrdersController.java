package com.food.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.food.entity.AddressBook;
import com.food.entity.Orders;
import com.food.service.AddressBookService;
import com.food.service.OrdersService;
import com.food.utils.Result;
import com.food.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/order")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;
    @Autowired
    private AddressBookService addressBookService;

    @PostMapping("/submit")
    public Result<Orders> saveOrder(@RequestBody Orders orders){
        orders.setUserId((Long) ThreadLocalUtil.get("user"));
        orders.setNumber(UUID.randomUUID().toString());
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
//        从地址簿中获取其他信息
        Long addressBookId = orders.getAddressBookId();
        AddressBook addressBook = addressBookService.getById(addressBookId);
        orders.setPhone(addressBook.getPhone());
        orders.setAddress(addressBook.getDetail());
        orders.setUserName(addressBook.getConsignee());

        ordersService.save(orders);
        return Result.success(orders);
    }

    @GetMapping("/userPage")
    public Result<IPage<Orders>> getCurrentUserOrdersById(@RequestParam Long page,@RequestParam Long pageSize){
        Long userId = (Long) ThreadLocalUtil.get("user");
        Page<Orders> ordersPage = new Page<>(page,pageSize);
        LambdaQueryWrapper<Orders> ordersLambdaQueryWrapper = new LambdaQueryWrapper<>();
        ordersLambdaQueryWrapper.eq(Orders::getUserId,userId).orderByDesc(Orders::getCheckoutTime);
        ordersService.page(ordersPage,ordersLambdaQueryWrapper);

        return Result.success(ordersPage);
    }

    @GetMapping("/page")
    public Result<IPage<Orders>> getOrdersPage(@RequestParam Long page,
                                               @RequestParam Long pageSize,
                                               @RequestParam(required = false) String number,
                                               @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime beginTime,
                                               @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime){
        Page<Orders> ordersPage = new Page<>(page,pageSize);
        LambdaQueryWrapper<Orders> ordersLambdaQueryWrapper = new LambdaQueryWrapper<>();

        ordersLambdaQueryWrapper
                .like(StringUtils.hasText(number),Orders::getNumber,number)
                .between(beginTime!=null&&endTime!=null,Orders::getOrderTime,beginTime,endTime)
                .orderByDesc(Orders::getCheckoutTime);
        ordersService.page(ordersPage,ordersLambdaQueryWrapper);

        return Result.success(ordersPage);

    }
}
