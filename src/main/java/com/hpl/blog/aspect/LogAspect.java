package com.hpl.blog.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Slf4j      //日志
@Aspect
@Component
public class LogAspect {

    //获取日志，目的是记录异常到日志中
    //private final Logger logger = LoggerFactory.getLogger(this.getClass());


    //定义一个切面， execution是指拦截哪些类（访问修饰符 包名.类名.方法）
    @Pointcut("execution(* com.hpl.blog.web.front.*.*(..))")
    public void log(){}

    //切面之前执行
    @Before("log()")
    public void doBefore(JoinPoint joinPoint){      //JoinPoint对象封装了SpringAop中切面方法的信息
        log.info("-----------doBefore...----------");
        //logger.info("-----------doBefore...----------");

        //1、接收请求的上下文（ServletRequestAttributes获取）
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        //2、获取url、ip、包名+方法名字
        String url = request.getRequestURL().toString();
        String ip = request.getRemoteAddr();
        String classMethod = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        //3、传入到自定义的RequestLog对象中
        RequestLog requestLog = new RequestLog(url,ip,classMethod,args);
        log.info("Request: {}",requestLog);
    }

    //切面之后执行
    @After("log()")
    public void doAfter(){
        log.info("-----------doAfter----------");
        //logger.info("-----------doAfter----------");
    }

    //切面的返回值
    @AfterReturning(returning = "res",pointcut = "log()")
    public void AfterReturn(Object res){
        log.info("-----------AfterReturn----------");
        log.info("Result: {}",res.toString());
    }

    //定义一个内部类，记录访问切面时的相关信息
    private class RequestLog{
        private String url;             //url
        private String ip;              //访问者
        private String classMethod;    //调用的方法
        private Object[] args;          //传递的参数

        //构造传参赋值
        public RequestLog(String url, String ip, String classMethod, Object[] args) {
            this.url = url;
            this.ip = ip;
            this.classMethod = classMethod;
            this.args = args;
        }

        @Override
        public String toString() {
            return "{" +
                    "url='" + url + '\'' +
                    ", ip='" + ip + '\'' +
                    ", classMethod='" + classMethod + '\'' +
                    ", args=" + Arrays.toString(args) +
                    '}';
        }
    }
}
