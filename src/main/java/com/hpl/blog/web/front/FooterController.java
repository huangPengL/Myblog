package com.hpl.blog.web.front;

import com.hpl.blog.po.Comment;
import com.hpl.blog.service.BlogsService;
import com.hpl.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/front")
public class FooterController {

    @Autowired
    private BlogsService blogsService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/footerData")
    public String footerData(Model model){

        //总浏览数
        model.addAttribute("totalViewNum",blogsService.blogTotalViewNum());

        //文章总数
        model.addAttribute("totalBlogNum",blogsService.blogCount());

        //评论总数
        model.addAttribute("totalCommentNum",commentService.commentCount());

        return "front/_fragments :: footerDataFragments";
    }
}
