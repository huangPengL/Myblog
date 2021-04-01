package com.hpl.blog.service;

import com.hpl.blog.NotFoundException_self;
import com.hpl.blog.po.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


public interface TagsService {

    //新增一条关于“标签”的数据
    Tag saveTag(Tag tag);

    //获得一条关于“标签”的数据
    Tag getTag(Long id);

    //更新一条关于“标签”的数据
    Tag updateTag(Long id,Tag tag);

    //删除一条关于“标签”的数据
    void deleteTag(Long id) throws NotFoundException_self;

    //根据名字查询一条关于“标签”的数据
    Tag getTagByName(String name);

    //分页
    Page<Tag> listTag(Pageable pageable);

    //获取所有标签
    List<Tag> listTag();

    //根据一组id，返回一组Tag对象
    List<Tag> listTag(String ids);

    //获取标签对应的博客数量的前size个
    List<Tag> listTagTop(Integer size);

    //获取所有以博客数量为降序的标签
    List<Tag> listTagOrderbyBlogSize();

}
