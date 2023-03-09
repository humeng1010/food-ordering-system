package com.food.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.food.entity.Employee;
import com.food.utils.Result;

import javax.servlet.http.HttpServletRequest;

public interface EmployeeService extends IService<Employee> {
    Result<Employee> login(HttpServletRequest request, Employee employee);

    Result<String> logout(HttpServletRequest request);


    Result<String> saveEmployee(HttpServletRequest request, Employee employee);

    Result<IPage<Employee>> getEmployeeByPageCondition(Integer page, Integer pageSize, String name);
}
