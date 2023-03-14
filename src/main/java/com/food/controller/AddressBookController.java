package com.food.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.food.entity.AddressBook;
import com.food.service.AddressBookService;
import com.food.utils.Result;
import com.food.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/addressBook")
public class AddressBookController {

    @Autowired
    AddressBookService addressBookService;

    @PostMapping
    public Result<AddressBook> save(@RequestBody AddressBook addressBook){
        addressBook.setUserId((Long) ThreadLocalUtil.get("user"));
//        addressBook.setCreateUser((Long) ThreadLocalUtil.get("user"));
        addressBookService.save(addressBook);
        return Result.success(addressBook);
    }

    @PutMapping("/default")
    public Result<AddressBook> setDefault(@RequestBody AddressBook addressBook){
        LambdaUpdateWrapper<AddressBook> addressBookLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        addressBookLambdaUpdateWrapper.set(AddressBook::getIsDefault,0).eq(AddressBook::getUserId,ThreadLocalUtil.get("user"));
        addressBookService.update(addressBookLambdaUpdateWrapper);

        addressBook.setIsDefault(1);
        addressBookService.updateById(addressBook);
        return Result.success(addressBook);
    }

    @GetMapping("/list")
    public Result<List<AddressBook>> list(AddressBook addressBook){
        addressBook.setUserId((Long) ThreadLocalUtil.get("user"));
        LambdaQueryWrapper<AddressBook> addressBookLambdaQueryWrapper = new LambdaQueryWrapper<>();
        addressBookLambdaQueryWrapper.eq(addressBook.getUserId()!=null,AddressBook::getUserId,addressBook.getUserId())
                        .orderByDesc(AddressBook::getUpdateTime);

        List<AddressBook> addressBookList = addressBookService.list(addressBookLambdaQueryWrapper);

        return Result.success(addressBookList);
    }

}
