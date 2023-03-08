package com.food.interceptor;

import com.alibaba.fastjson.JSON;
import com.food.entity.Employee;
import com.food.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Objects;

/**
 * 登陆拦截器
 */
@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        log.info("拦截到的请求:{}",requestURI);
        HttpSession session = request.getSession();

        Object empId = session.getAttribute("employee");

        if (Objects.isNull(empId)){
            //如果没有该session,则需要登陆
//            response.sendRedirect("/backend/page/login/login.html");
//            给页面响应没有登陆信息,前端页面自动跳转到登录页
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(JSON.toJSONString(Result.fail("NOTLOGIN")));
//            拦截
            return false;
        }
        return true;
    }
}
