package com.hpl.blog.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor extends HandlerInterceptorAdapter {

    //请求到达之前，做处理拦截。
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        //若未登录，访问/back下的路径，则拦截访问（重定向到/back路径，返回false），否则可以访问（返回true）
        if(request.getSession().getAttribute("user") == null){
            response.sendRedirect("/back");
            return false;
        }
        return true;
    }
}
