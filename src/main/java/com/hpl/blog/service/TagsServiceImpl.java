package com.hpl.blog.service;

import com.hpl.blog.NotFoundException_self;
import com.hpl.blog.dao.TagsRepository;
import com.hpl.blog.po.Tag;
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
public class TagsServiceImpl implements TagsService {

    @Autowired
    TagsRepository tagsRepository;

    /**
     * 新增一条关于“标签”的数据
     * @param tag
     * @return
     */
    @Override
    public Tag saveTag(Tag tag) {
        return tagsRepository.save(tag);
    }


    /**
     * 获得一条关于“标签”的数据
     * @param id
     * @return
     */
    @Override
    public Tag getTag(Long id) {
        return tagsRepository.findById(id).get();
    }

    @Override
    public Tag updateTag(Long id, Tag tag) {
        Tag ans = tagsRepository.findById(id).get();

        if(ans != null){
            BeanUtils.copyProperties(tag,ans);    //将type的数据复制到tempType中
            return tagsRepository.save(ans);    //由于tempType中有id，所以会进行更新的动作
        }
        else {
            throw new NotFoundException_self("不存在该类型");
        }
    }

    @Override
    public void deleteTag(Long id) {
        tagsRepository.deleteById(id);
    }

    @Override
    public Tag getTagByName(String name) {
        return tagsRepository.findByName(name);
    }

    @Override
    @Transactional
    public Page<Tag> listTag(Pageable pageable) {
        return tagsRepository.findAll(pageable);
    }

    @Override
    public List<Tag> listTag() {
        return tagsRepository.findAll();
    }


    /**
     * 根据传递过来的字符串（以逗号隔开的id）转化为一个List的id，然后执行查询
     * @param ids
     * @return
     */
    @Override
    public List<Tag> listTag(String ids) {  //ids = "1,2,3,4..."
        return tagsRepository.findAllById(convertToList(ids));
    }

    //将字符串中以逗号隔开的数字，转换为List
    private List<Long> convertToList(String ids){
        List<Long> TagsList = new ArrayList<>();
        if("".equals(ids) != true && ids!=null){
            String []idArray = ids.split(",");
            for(int i=0;i<idArray.length;i++) {
                TagsList.add(new Long(idArray[i]));
            }
        }
        return TagsList;
    }

    @Override
    public List<Tag> listTagTop(Integer size) {
        //自定义排序（按照tag.blogs.size降序排序）
        Sort sortby = Sort.by(Sort.Direction.DESC,"blogs.size");
        //分页的方式（第一页，前面size个，以自定义排序的方式）
        Pageable pageable = PageRequest.of(0,size,sortby);
        return tagsRepository.findTop(pageable);
    }

    @Override
    public List<Tag> listTagOrderbyBlogSize() {
        List<Tag> listTag = tagsRepository.findAll();
        Collections.sort(listTag, new Comparator<Tag>() {
            @Override
            public int compare(Tag o1, Tag o2) {
                if(o1.getBlogs().size() > o2.getBlogs().size()){
                    return -1;
                }
                else if(o1.getBlogs().size() < o2.getBlogs().size()){
                    return 1;
                }
                else{
                    return 0;
                }
            }
        });
        return listTag;
    }
}
