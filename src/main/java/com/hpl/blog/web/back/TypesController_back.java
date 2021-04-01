package com.hpl.blog.web.back;


import com.hpl.blog.po.Type;
import com.hpl.blog.service.TypesService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/back")
public class TypesController_back {

    @Autowired
    private TypesService typesService;


    /**
     * 跳转到分类列表页面时，执行分页查询（根据id升序排序）后将结果放入Model中
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping("/types")
    public String typesPage(
            @PageableDefault(size = 5,sort = {"id"},direction = Sort.Direction.DESC) Pageable pageable,
            Model model){

        model.addAttribute("page",typesService.listType(pageable));

        return "back/types";
    }

    /**
     * 跳转到分类新增页面
     * @return
     */
    @GetMapping("/types-post")
    public String types_postPage() {
        return "back/types-post";
    }


    /**
     * 新增一条分类
     * @param type
     * @param attributes
     * @return
     */
    @PostMapping("/inputType")
    public String inputType(Type type, RedirectAttributes attributes){

        //重名验证（查询到数据表示有重名的现象）
        Type sameName_test = typesService.getTypeByName(type.getName());
        if(sameName_test != null){
            attributes.addFlashAttribute("sameNameMessage","新增失败！名字重复");
            return "redirect:/back/types-post";
        }

        //新增（新增后有返回数据表示新增成功）
        Type type1 = typesService.saveType(type);
        if(type1 != null){
            attributes.addFlashAttribute("message","恭喜！新增一条名为["+ type.getName() +"]的分类!");
        }
        return "redirect:/back/types";
    }

    /**
     * 跳转到编辑分类
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/types/{id}/edit")
    public String types_editPage(@PathVariable Long id,Model model){

        //根据传来的Id进行查询，把对应的类传递到types-edit页面中
        model.addAttribute("type",typesService.getType(id));
        return "back/types-edit";
    }

    /**
     * 编辑一条分类
     * @param type
     * @param attributes
     * @return
     */
    @PostMapping("/editType/{id}")
    public String editType(@PathVariable Long id, Type type, RedirectAttributes attributes) throws NotFoundException {

        //重名验证（查询到数据表示有重名的现象）。
        // 若重名验证通过则进行更新操作，否则记录错误信息，并且重新跳转到本页面
        Type sameNameTest = typesService.getTypeByName(type.getName());
        if(sameNameTest != null){
            attributes.addFlashAttribute("sameNameMessage","编辑失败！名字重复");
            return "redirect:/back/types/{id}/edit";
        }

        //更新（更新后有返回数据表示更新成功）
        //若更新成功，返回到types.html页面。
        Type type1 = typesService.updateType(id,type);
        if(type1 != null){
            attributes.addFlashAttribute("message","恭喜！将分类名修改为["+ type.getName() + "]!");
        }
        return "redirect:/back/types";
    }


    /**
     * 删除一个分类
     * @param id
     * @return
     */
    @GetMapping("/types/{id}/delete")
    public String deleteType(@PathVariable Long id, RedirectAttributes attributes){
        String typename = typesService.getType(id).getName();
        typesService.deleteType(id);
        attributes.addFlashAttribute("message","恭喜！删除一条名为["+ typename + "]的分类!");
        return "redirect:/back/types";
    }
}
