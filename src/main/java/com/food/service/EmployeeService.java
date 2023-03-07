package com.food.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.food.entity.Employee;
import com.food.utils.Result;

import javax.servlet.http.HttpServletRequest;

public interface EmployeeService extends IService<Employee> {
    Result<Employee> login(HttpServletRequest request, Employee employee);
}
