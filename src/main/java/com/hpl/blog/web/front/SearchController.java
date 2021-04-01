package com.hpl.blog.web.front;

import com.hpl.blog.service.BlogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/front")
public class SearchController {

    @Autowired
    private BlogsService blogsService;

    @PostMapping("/search")
    public String search(@PageableDefault(size = 100,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable,
                         @RequestParam String searchText, Model model){

        model.addAttribute("page",blogsService.listBlog("%"+ searchText +"%",pageable));
        model.addAttribute("searchText",searchText);
        return "front/search";
    }

}
