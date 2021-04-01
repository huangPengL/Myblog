package com.hpl.blog.web.front;

import com.hpl.blog.po.Tag;
import com.hpl.blog.service.BlogsService;
import com.hpl.blog.service.TagsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/front")
public class TagsController_front {

    @Autowired
    private TagsService tagsService;

    @Autowired
    private BlogsService blogsService;

    @GetMapping("/tags/{tagId}")
    public String tags(@PageableDefault(size = 5,sort = {"viewNum"},direction = Sort.Direction.DESC) Pageable pageable,
                       @PathVariable Long tagId, Model model){

        List<Tag> listTag = tagsService.listTagOrderbyBlogSize();
        if(tagId == -1)
            tagId = listTag.get(0).getId();
        model.addAttribute("tags",listTag);
        model.addAttribute("page",blogsService.listBlogByTagId(tagId,pageable));
        model.addAttribute("activeTagId",tagId);

        return "front/tags";
    }
}
