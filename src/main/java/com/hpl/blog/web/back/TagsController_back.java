package com.hpl.blog.web.back;

import com.hpl.blog.dao.TagsRepository;
import com.hpl.blog.po.Tag;
import com.hpl.blog.service.TagsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.websocket.server.PathParam;

@Controller
@RequestMapping("/back")
public class TagsController_back {

    @Autowired
    private TagsService tagsService;


    /**
     * 跳转到【标签列表】页面时，执行分页查询（根据id升序排序）后将结果放入Model中
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping("/tags")
    public String tags(@PageableDefault(size=5,sort = {"id"},direction = Sort.Direction.DESC)Pageable pageable,
                       Model model){
        model.addAttribute("page",tagsService.listTag(pageable));
        return "back/tags";
    }

    /**
     * 跳转到【标签新增】页面
     * @return
     */
    @GetMapping("/Totags-post")
    public String tags_postPage(){
        return "back/tags-post";
    }


    /**
     * 新增一条标签
     * @param tag
     * @param attributes
     * @return
     */
    @PostMapping("/inputTag")
    public String inputTag(Tag tag, RedirectAttributes attributes){

        //重名验证（查询到数据表示有重名的现象）
        Tag temptag = tagsService.getTagByName(tag.getName());
        if(temptag != null){
            attributes.addFlashAttribute("sameNameMessage","新增失败！名字重复");
            return "redirect:/back/Totags-post";
        }

        //新增（新增后有返回数据表示新增成功）
        Tag tag1 = tagsService.saveTag(tag);
        if(tag1 != null){
            attributes.addFlashAttribute("message","恭喜！新增一条名为["+ tag.getName() +"]的标签!");
        }
        return "redirect:/back/tags";
    }

    /**
     * 跳转到编辑标签页面
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/tags/{id}/edit")
    public String tags_editPage(@PathVariable Long id,Model model){
        model.addAttribute("tag",tagsService.getTag(id));
        return "back/tags-edit";
    }

    /**
     * 编辑一条标签
     * @param id
     * @param tag
     * @param attributes
     * @return
     */
    @PostMapping("/editTag/{id}")
    public String editTag(@PathVariable Long id,Tag tag,RedirectAttributes attributes){
        //重名验证
        Tag sameNameTest = tagsService.getTagByName(tag.getName());
        if(sameNameTest!=null){
            attributes.addFlashAttribute("sameNameMessage","编辑失败！名字重复");
            return "redirect:/back/tags/{id}/edit";
        }

        //更新
        Tag tag1 = tagsService.updateTag(id,tag);
        if(tag1 != null){
            attributes.addFlashAttribute("message","恭喜！将标签名修改为["+ tag.getName() + "]!");
        }
        return "redirect:/back/tags";
    }

    /**
     * 删除一个标签
     * @param id
     * @param attributes
     * @return
     */
    @GetMapping("/tags/{id}/delete")
    public String deleteTag(@PathVariable Long id,RedirectAttributes attributes){
        String TagName = tagsService.getTag(id).getName();
        tagsService.deleteTag(id);
        attributes.addFlashAttribute("message","恭喜！删除一条名为["+ TagName + "]的标签!");
        return "redirect:/back/tags";

    }

}
