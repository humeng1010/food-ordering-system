package com.food.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.food.entity.Employee;
import com.food.exception.DuplicateException;
import com.food.mapper.EmployeeMapper;
import com.food.service.EmployeeService;
import com.food.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;

@Service
@Slf4j
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService  {

    @Override
    public Result<Employee> login(HttpServletRequest request, Employee employee) {
        String username = employee.getUsername();
        String password = employee.getPassword();
//        加密密码与数据库中的密码对比
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,username).eq(Employee::getPassword,password);
        Employee obj = this.getOne(queryWrapper);

        if (Objects.isNull(obj)){
            //如果没有用户
            return Result.fail("没有该用户或账户密码错误");
        }

        if (obj.getStatus() == 0){
            //该用户被禁用
            return Result.fail("该用户被禁用");
        }

        HttpSession session = request.getSession();
        session.setAttribute("employee",obj.getId());

        return Result.success(obj,"登陆成功");
    }

    @Override
    public Result<String> logout(HttpServletRequest request) {
        request.getSession().removeAttribute("employee");
        return Result.success("退出成功");
    }

    @Override
    public Result<IPage<Employee>> getEmployeeByPage(Integer page, Integer pageSize) {
        IPage<Employee> iPage = new Page<>(page,pageSize);

        IPage<Employee> employeeIPage = this.page(iPage);

        return Result.success(employeeIPage);
    }

    @Override
    public Result<String> saveEmployee(HttpServletRequest request,Employee employee) {
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = this.getOne(queryWrapper);
        if (!Objects.isNull(emp)){
            throw new DuplicateException("账号重复");
        }
//        设置初始密码
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
//        使用mp进行自动填充
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
        Long empId = (Long) request.getSession().getAttribute("employee");
        employee.setCreateUser(empId);
        employee.setUpdateUser(empId);
        boolean flag = this.save(employee);
        if (!flag){
            return Result.fail("添加失败");
        }
        return Result.success("添加成功");
    }
}
