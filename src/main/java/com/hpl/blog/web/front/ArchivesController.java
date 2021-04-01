package com.hpl.blog.web.front;

import com.hpl.blog.po.Blog;
import com.hpl.blog.service.BlogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/front")
public class ArchivesController {

    @Autowired
    private BlogsService blogsService;

    @GetMapping("/archives")
    public String archives(Model model){

        Map<String, List<Blog>> mapBlog;
        mapBlog = blogsService.MapArchivesBlog();
        model.addAttribute("mapBlog",mapBlog);

        model.addAttribute("blogCount",blogsService.blogCount());
        return "front/archives";
    }

}
