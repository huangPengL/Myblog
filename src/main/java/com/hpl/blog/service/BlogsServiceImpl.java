package com.hpl.blog.service;

import com.hpl.blog.NotFoundException_self;
import com.hpl.blog.dao.BlogsRepository;
import com.hpl.blog.po.Blog;
import com.hpl.blog.po.Type;
import com.hpl.blog.utils.MarkdownUtils;
import com.hpl.blog.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.*;


@Service
public class BlogsServiceImpl implements BlogsService{

    @Autowired
    private BlogsRepository blogsRepository;

    /**
     * 后端展示博客信息（不需要转化格式）
     * @param id
     * @return
     */
    @Override
    public Blog getBlog_back(Long id) {
        return blogsRepository.findById(id).get();
    }

    /**
     * 前端展示博客信息（需要把blog.comment的markdown转化为html格式）
     * @param id
     * @return
     */
    @Override
    public Blog getBlog_front(Long id) {
        Blog blog = blogsRepository.findById(id).get();
        //blog.setContent(MarkdownUtils.markdown_to_html_Extensions(blog.getContent()));     //这样操作可能会更改数据库的字段
        if(blog == null){
            throw new NotFoundException_self("博客不存在");
        }
        Blog ans = new Blog();
        BeanUtils.copyProperties(blog,ans);
        ans.setContent(MarkdownUtils.markdown_to_html_Extensions(ans.getContent()));
        return ans;
    }

    /**
     * 分页动态组合查询
     * @param pageable
     * @param blog
     * @return
     */
    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog) {
        return blogsRepository.findAll(new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                //定义一些条件语句
                if("".equals(blog.getTitle()) != true && blog.getTitle() != null){
                    predicates.add(criteriaBuilder.like(root.<String>get("title"),"%"+blog.getTitle()+"%"));
                }
                if(blog.getTypeId() != null){
                    predicates.add(criteriaBuilder.equal(root.<Type>get("type").get("id"),blog.getTypeId()));
                }
               if(blog.isRecommendState() == true){
                    predicates.add(criteriaBuilder.equal(root.<Boolean>get("recommendState"),blog.isRecommendState()));
                }


                criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        },pageable);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return blogsRepository.findAll(pageable);
    }

    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        //设置创建时间、更新时间、浏览次数
        blog.setCreateTime(new Date());
        blog.setUpdateTime(new Date());
        blog.setViewNum(0);

        return blogsRepository.save(blog);
    }

    @Transactional
    @Override
    public Blog updateBlog(Long id, Blog blog) {
        //ans是最终修改后的博客，blog是已经修改了的博客（blog还缺少创建时间，更新时间，浏览次数）
        Blog ans = blogsRepository.findById(id).get();

        //设置blog的创建时间、更新时间、浏览次数
        blog.setCreateTime(ans.getCreateTime());
        blog.setUpdateTime(new Date());
        blog.setViewNum(ans.getViewNum());

        //将blog覆盖ans
        if(ans != null){
            BeanUtils.copyProperties(blog,ans);

            return blogsRepository.save(ans);
        }
        else{
            throw new NotFoundException_self("不存在该类型");
        }
    }

    @Transactional
    @Override
    public void deleteBlog(Long id) {
        blogsRepository.deleteById(id);
    }

    @Override
    public List<Blog> listRecommendBlogTop(Integer size) {
        //自定义排序（按照blog.updateTime降序排序）
        Sort sortBy = Sort.by(Sort.Direction.DESC,"updateTime");
        //分页的方式（第一页，前面size个，以自定义排序的方式）
        Pageable pageable = PageRequest.of(0,size,sortBy);
        return blogsRepository.findTop(pageable);
    }

    @Override
    public List<Blog> listBlog() {
        return blogsRepository.findAll();
    }

    /**
     * 根据传递的字符串searchText,对t_blog进行title或content字段的模糊查询
     * @param searchText
     * @param pageable
     * @return
     */
    @Override
    public Page<Blog> listBlog(String searchText, Pageable pageable) {
        return blogsRepository.findBySearchText(searchText,pageable);
    }

    @Override
    public Page<Blog> listBlogByTypeId(Long typeId, Pageable pageable) {
        return blogsRepository.findByTypeId(typeId,pageable);
    }

    @Override
    public Page<Blog> listBlogByTagId(Long tagId, Pageable pageable) {
        return blogsRepository.findAll(new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Join join = root.join("tags");
                return criteriaBuilder.equal(join.get("id"),tagId);
            }
        },pageable);
    }

    @Override
    public Map<String, List<Blog>> MapArchivesBlog() {

        //1、分组查询获得 博客更新时间的年份
        List<String> yearList = blogsRepository.findGroupbyYear();

        //2、将年份作为key，获得对应年份的blog列表
        Map<String, List<Blog>> mapBlog = new HashMap<>();
        for(String year : yearList){
            List<Blog> listBlog = blogsRepository.findByYear(year);
            //将blog列表按照年份key 放入map中
            mapBlog.put(year,listBlog);
        }
        return mapBlog;
    }

    @Override
    public int updateBlogViewNum(Long blogId) {
        return blogsRepository.updateViewNum(blogId);
    }

    @Override
    public Long blogCount() {
        return blogsRepository.count();
    }

    @Override
    public Long blogTotalViewNum() {
        return blogsRepository.getSumViewNum();
    }
}
