package com.hpl.blog.web.back;


import com.hpl.blog.po.User;
import com.hpl.blog.service.UserService;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/back")        //表示类中的所有响应请求的方法都是以该地址作为父路径
public class LoginController {

    @Autowired
    private UserService userService;

    /**
     * 【跳转到登录页面】
     * @return
     */
    //若来自/back下的请求会跳到“登录页面”
    @GetMapping
    public String loginPage(){
        return "back/login";
    }

    /**
     * 【登入验证】
     * @param username
     * @param password
     * @param session
     * @param attributes
     * @return
     */
    //若来自/back/login的请求会调用该控制器，即“登陆验证”
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes attributes){

        User user = userService.checkUser(username,password);
        if(user != null){
            //安全起见，把密码置空
            user.setPassword(null);
            session.setAttribute("user",user);
            return "back/index";
        }
        else{
            //将message传到前端页面
            attributes.addFlashAttribute("message","用户名和密码错误");
            //返回到/back路径下，即loginPage()
            return "redirect:/back";
        }
    }

    /**
     * 【登出操作】
     * @param session
     * @return
     */
    //若来自/back/logout的请求会调用该控制器，即“登陆验证”
    @GetMapping("/logout")
    public String Logout(HttpSession session){
        session.removeAttribute("user");
        //返回到/back路径下，即loginPage()
        return "redirect:/back";
    }

}
