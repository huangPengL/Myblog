package com.hpl.blog.service;

import com.hpl.blog.po.Type;
import javassist.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TypesService {

    //新增一条关于“类别”的数据
    Type saveType(Type type);

    //获得一条关于“类别”的数据
    Type getType(Long id);

    //更新一条关于“类别”的数据
    Type updateType(Long id,Type type) throws NotFoundException;

    //删除一条关于“类别”的数据
    void deleteType(Long id);

    //根据名字查询一条关于“类别”的数据
    Type getTypeByName(String name);

    //分页获取所有类别
    Page<Type> listType(Pageable pageable);

    //获取所有类别(以对应博客数量的降序)
    List<Type> listTypeOrderbyBlogsSize();

    //获取类别对应的博客数量的前size个
    List<Type> listTypeTop(Integer size);

}
