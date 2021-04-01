package com.hpl.blog.web.front;


import com.hpl.blog.NotFoundException_self;
import com.hpl.blog.po.Blog;
import com.hpl.blog.service.BlogsService;
import com.hpl.blog.service.TagsService;
import com.hpl.blog.service.TypesService;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class indexController {

    //获取日志，目的是记录异常到日志中
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BlogsService blogsService;

    @Autowired
    private TypesService typesService;

    @Autowired
    private TagsService tagsService;


    @GetMapping("/")
    public String index(@PageableDefault(size = 7,sort = {"viewNum"},direction = Sort.Direction.DESC)Pageable pageable,
                        Model model){
        model.addAttribute("page",blogsService.listBlog(pageable));
        model.addAttribute("types",typesService.listTypeTop(6));
        model.addAttribute("tags",tagsService.listTagTop(10));
        model.addAttribute("blogs",blogsService.listRecommendBlogTop(5));
        return "front/index";
    }



}
