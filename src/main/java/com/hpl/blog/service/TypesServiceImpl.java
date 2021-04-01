package com.hpl.blog.service;

import com.hpl.blog.NotFoundException_self;
import com.hpl.blog.dao.TypesRepository;
import com.hpl.blog.po.Type;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


@Service
public class TypesServiceImpl implements TypesService {

    @Autowired
    private TypesRepository typesRepository;

    /**
     * 新增一条关于“类别”的数据
     * @param type
     * @return
     */
    @Override
    @Transactional
    public Type saveType(Type type) {
        return typesRepository.save(type);
    }

    /**
     * 获得一条关于“类别”的数据
     * @param id
     * @return
     */
    @Override
    @Transactional
    public Type getType(Long id) {
        return typesRepository.findById(id).get();
    }

    /**
     * 更新一条关于“类别”的数据
     * @param id
     * @param type
     * @return
     */
    @Override
    @Transactional
    public Type updateType(Long id, Type type){
        Type tempType = typesRepository.findById(id).get();

        if(tempType != null){
            BeanUtils.copyProperties(type,tempType);    //将type的数据复制到tempType中
            return typesRepository.save(tempType);    //由于tempType中有id，所以会进行更新的动作
        }
        else {
            throw new NotFoundException_self("不存在该类型");
        }
    }

    /**
     * 删除一条关于“类别”的数据
     * @param id
     */
    @Override
    @Transactional
    public void deleteType(Long id) {
        typesRepository.deleteById(id);
    }


    /**
     * 根据名字查询一条关于“类别”的数据
     * @param name
     * @return
     */
    @Override
    public Type getTypeByName(String name) {
        return typesRepository.findByName(name);
    }

    /**
     * 分类获取所有类别
     * @param pageable
     * @return
     */
    @Override
    @Transactional
    public Page<Type> listType(Pageable pageable) {
        return typesRepository.findAll(pageable);
    }

    /**
     * 获取所有类别(以对应博客数量的降序)
     * @return
     */
    @Override
    public List<Type> listTypeOrderbyBlogsSize() {
        List<Type> listType = typesRepository.findAll();
        Collections.sort(listType,new Comparator<Type>(){
            @Override
            public int compare(Type t1,Type t2){
                int dist = t1.getBlogs().size() - t2.getBlogs().size();
                if(dist>0){
                    return -1;
                }
                else if(dist<0){
                    return 1;
                }
                else{
                    return 0;
                }
            }
        });
        return listType;
    }

    /**
     * 获取类别对应的博客数量最多的前size个
     * @param size
     * @return
     */
    @Override
    public List<Type> listTypeTop(Integer size) {
        //自定义排序（按照type.blogs.size降序排序）
        Sort sortBy = Sort.by(Sort.Direction.DESC,"blogs.size");
        //分页的方式（第一页，前面size个，以自定义排序的方式）
        Pageable pageable = PageRequest.of(0,size,sortBy);
        return typesRepository.findTop(pageable);
    }
}
