package com.hpl.blog.web.front;

import com.hpl.blog.po.Type;
import com.hpl.blog.service.BlogsService;
import com.hpl.blog.service.TypesService;
import javafx.collections.transformation.SortedList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/front")
public class TypesController_front {

    @Autowired
    private TypesService typesService;

    @Autowired
    private BlogsService blogsService;

    /**
     * 跳转/刷新 博客分类页面
     * @param pageable
     * @param typeId
     * @param model
     * @return
     */
    @GetMapping("/types/{typeId}")
    public String types(@PageableDefault(size = 4,sort = {"viewNum"},direction = Sort.Direction.DESC) Pageable pageable,
                        @PathVariable Long typeId, Model model){

        List<Type> listType = typesService.listTypeOrderbyBlogsSize();
        //导航栏中点击分类时候，传递的typeId为-1，对应选中最热门的标签
        if(typeId == -1)
            typeId = listType.get(0).getId();

        model.addAttribute("page",blogsService.listBlogByTypeId(typeId,pageable));
        model.addAttribute("types",listType);
        model.addAttribute("activeTypeId",typeId);
        return "front/types";
    }
}
