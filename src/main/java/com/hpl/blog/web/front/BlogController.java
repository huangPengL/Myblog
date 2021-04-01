package com.hpl.blog.web.front;

import com.hpl.blog.po.Blog;
import com.hpl.blog.service.BlogsService;
import com.hpl.blog.service.CommentService;
import com.hpl.blog.utils.MarkdownUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/front")
public class BlogController {

    @Autowired
    private BlogsService blogsService;

    @Autowired
    private CommentService commentService;

    /**
     * 博客详情。（博客内容，博客评论信息）
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/blog/{id}")
    public String DisplayBlogById(@PathVariable Long id,Model model){
        Blog blog = blogsService.getBlog_front(id);
        blogsService.updateBlogViewNum(id);             //博客访问次数+1
        model.addAttribute("blog",blog);
        return "front/blog";
    }
}
