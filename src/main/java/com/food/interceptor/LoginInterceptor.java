package com.food.interceptor;

import com.alibaba.fastjson.JSON;
import com.food.utils.Result;
import com.food.utils.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
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
        HttpSession session = request.getSession();

//        判断员工是否登陆
        Object empId = session.getAttribute("employee");
        if (!Objects.isNull(empId)){
            //保存到当前线程变量中
            ThreadLocalUtil.put("employee",empId);
            return true;
        }

//        判断用户是否登陆
        Object userId = session.getAttribute("user");
        if (!Objects.isNull(userId)){
            //保存到当前线程变量中
            ThreadLocalUtil.put("user", userId);
            return true;
        }
//            拦截
        log.info("拦截到的请求:{}",requestURI);
//            给页面响应没有登陆信息,前端页面自动跳转到登录页
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JSON.toJSONString(Result.fail("NOTLOGIN")));
        return false;

    }
}
