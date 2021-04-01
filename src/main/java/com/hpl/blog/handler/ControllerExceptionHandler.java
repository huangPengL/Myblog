package com.hpl.blog.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ControllerExceptionHandler {

    //获取日志，目的是记录异常到日志中
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //注解。拦截异常异常信息
    @ExceptionHandler(Exception.class)
    public ModelAndView exceptionHandler(HttpServletRequest request,Exception e) throws Exception {

        //1、记录异常到日志中
        logger.error("Request URL: {}, Exception: {}",request.getRequestURL(),e);

        //2、返回到 指定状态码的页面 或 自定义的错误页面error.html 或 指定状态码的页面

        //2.1 若异常有状态码（如404等，则抛出异常让springboot指定到跳转的页面）
        if(AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null){
            throw e;
        }

        //2.2 若异常没有状态码，则返回错误页面
        ModelAndView mv = new ModelAndView();
        mv.addObject("url",request.getRequestURL());
        mv.addObject("exception",e);
        mv.setViewName("error/error");
        return mv;
    }
}
