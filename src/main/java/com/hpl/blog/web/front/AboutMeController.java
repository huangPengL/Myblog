package com.hpl.blog.web.front;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/front")
public class AboutMeController {

    @GetMapping("/aboutMe")
    public String aboutMe(){
        return "front/aboutMe";
    }
}
