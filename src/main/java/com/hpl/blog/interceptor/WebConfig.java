package com.hpl.blog.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.ArrayList;
import java.util.List;


//配置类
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter{

    //将定义好的拦截器注册
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        //不拦截以下路径
        List<String> excludeList = new ArrayList<>();
        excludeList.add("/back");
        excludeList.add("/back/login");

        //拦截以下路径
        List<String> includeList = new ArrayList<>();
        includeList.add("/back/**");

        //注册拦截器（拦截器，拦截路径，不拦截路径）
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns(includeList)
                .excludePathPatterns(excludeList);
    }
}
