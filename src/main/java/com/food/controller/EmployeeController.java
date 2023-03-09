package com.food.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.food.entity.Employee;
import com.food.service.EmployeeService;
import com.food.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/employee")
@Slf4j
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public Result<Employee> login(HttpServletRequest request,@RequestBody Employee employee){
        return employeeService.login(request,employee);
    }

    @PostMapping("/logout")
    public Result<String> logout(HttpServletRequest request){
        return employeeService.logout(request);
    }

    @PostMapping
    public Result<String> saveEmployee(HttpServletRequest request,@RequestBody Employee employee){
        return employeeService.saveEmployee(request,employee);
    }


    @GetMapping("/page")
    public Result<IPage<Employee>> pageByCondition(
            @RequestParam("page") Integer page,
            @RequestParam("pageSize") Integer pageSize,
            @RequestParam(value = "name",required = false) String name){
        return employeeService.getEmployeeByPageCondition(page,pageSize,name);
    }
}
