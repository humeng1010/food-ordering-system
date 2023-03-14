package com.food.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.food.entity.AddressBook;
import com.food.mapper.AddressBookMapper;
import com.food.service.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook>
        implements AddressBookService {

}
