package com.food.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.food.entity.Employee;
import com.food.utils.Result;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EmployeeService extends IService<Employee> {
    Result<Employee> login(HttpServletRequest request, Employee employee);

    Result<String> logout(HttpServletRequest request);

    Result<IPage<Employee>> getEmployeeByPage(Integer page, Integer pageSize);

    Result<String> saveEmployee(HttpServletRequest request, Employee employee);
}
