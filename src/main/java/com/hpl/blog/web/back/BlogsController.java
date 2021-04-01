package com.hpl.blog.web.back;


import com.hpl.blog.po.Blog;
import com.hpl.blog.po.Type;
import com.hpl.blog.po.User;
import com.hpl.blog.service.BlogsService;
import com.hpl.blog.service.TagsService;
import com.hpl.blog.service.TypesService;
import com.hpl.blog.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.data.domain.Pageable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/back")
public class BlogsController {

    @Autowired
    private BlogsService blogsService;

    @Autowired
    private TypesService typesService;

    @Autowired
    private TagsService tagsService;

    /**
     * 跳转到【博客列表】页面
     * @param pageable
     * @param blog
     * @param model
     * @return
     */
    @GetMapping("/blogs")
    public String blogs(@PageableDefault(size = 4,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable,
                        BlogQuery blog, Model model){
        model.addAttribute("types",typesService.listTypeOrderbyBlogsSize());
        model.addAttribute("page",blogsService.listBlog(pageable,blog));
        return "back/blogs";
    }

    /**
     * 执行【博客查询】，刷新表单
     * @param pageable
     * @param blog
     * @param model
     * @return
     */
    @PostMapping("/blogs/search")
    public String blogsSearch(@PageableDefault(size = 4,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable,
                              BlogQuery blog, Model model){
        model.addAttribute("page",blogsService.listBlog(pageable,blog));
        return "back/blogs :: blogList";        //刷新局部：blogList
    }

    /**
     * 跳转到【博客新增】页面
     * @return
     */
    @GetMapping("/Toblogs-post")
    public String blogs_postPage(Model model){
        model.addAttribute("types",typesService.listTypeOrderbyBlogsSize());
        model.addAttribute("tags",tagsService.listTag());
        return "back/blogs-post";
    }

    /**
     * 【创建】一篇博客
     * @param blog
     * @param attributes
     * @param session
     * @return
     */
    @PostMapping("/inputBlog")
    public String inputBlog(Blog blog, RedirectAttributes attributes, HttpSession session){

        //1、创建博客之前初始化
        blog.setUser((User)session.getAttribute("user"));             //设置User对象
        blog.setType(typesService.getType(blog.getTypeId()));           //设置Type对象
        blog.setTags(tagsService.listTag(blog.getTagIds()));            //设置Tags对象（一组Tag对象）

        //2、创建博客
        Blog Temp = blogsService.saveBlog(blog);
        if(Temp != null){
            attributes.addFlashAttribute("message","恭喜！新增一篇标题为["+ Temp.getTitle() +"]的博客!");
        }
        return "redirect:/back/blogs";
    }

    /**
     * 跳转到编辑博客页面
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/blogs/{id}/edit")
    public String blogs_editPage(@PathVariable Long id, Model model){
        //根据id查找blog,并对blog中的tags初始化
        Blog blog = blogsService.getBlog_back(id);
        blog.initTags();

        //根据blog中分类的id查找type并set到当前blog中以便前端通过blog.type.name来访问
        Type type = typesService.getType(blog.getType().getId());
        blog.setType(type);

        //
        model.addAttribute("types",typesService.listTypeOrderbyBlogsSize());
        model.addAttribute("tags",tagsService.listTag());

        model.addAttribute("blog",blog);
        return "back/blogs-edit";
    }

    /**
     * 【修改】一篇博客
     * @param blog
     * @param attributes
     * @param session
     * @return
     */
    @PostMapping("/editBlog/{id}")
    public String editBlog(@PathVariable Long id, Blog blog, RedirectAttributes attributes, HttpSession session){

        //1、更新博客之前初始化
        blog.setUser((User)session.getAttribute("user"));             //设置User对象
        blog.setType(typesService.getType(blog.getTypeId()));           //设置Type对象
        blog.setTags(tagsService.listTag(blog.getTagIds()));            //设置Tags对象（一组Tag对象）

        //2、更新博客
        Blog Temp = blogsService.updateBlog(id,blog);
        if(Temp != null){
            attributes.addFlashAttribute("message","恭喜！成功修改一篇标题为["+ Temp.getTitle() +"]的博客!");
        }
        return "redirect:/back/blogs";
    }


    /**
     * 【删除】一篇博客
     * @param id
     * @param attributes
     * @return
     */
    @GetMapping("/blogs/{id}/delete")
    public String deleteBlog(@PathVariable Long id,RedirectAttributes attributes){
        String blogTitle = blogsService.getBlog_back(id).getTitle();
        blogsService.deleteBlog(id);
        attributes.addFlashAttribute("message","恭喜！成功删除一篇标题为["+ blogTitle+"]的博客!");
        return "redirect:/back/blogs";
    }

}
