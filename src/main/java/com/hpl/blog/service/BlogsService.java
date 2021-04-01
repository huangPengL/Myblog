package com.hpl.blog.service;

import com.hpl.blog.NotFoundException_self;
import com.hpl.blog.po.Blog;
import com.hpl.blog.vo.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface BlogsService {

    Blog getBlog_back(Long id);

    Blog getBlog_front(Long id);

    List<Blog> listBlog();

    Page<Blog> listBlog(Pageable pageable,BlogQuery blog);

    Page<Blog> listBlog(Pageable pageable);

    Page<Blog> listBlog(String searchText, Pageable pageable);

    Page<Blog> listBlogByTypeId(Long typeId, Pageable pageable);

    Page<Blog> listBlogByTagId(Long typeId, Pageable pageable);

    Map<String,List<Blog>> MapArchivesBlog();

    Blog saveBlog(Blog blog);

    Blog updateBlog(Long id,Blog blog) throws NotFoundException_self;

    void deleteBlog(Long id);

    List<Blog> listRecommendBlogTop(Integer size);

    int updateBlogViewNum(Long blogId);

    Long blogCount();

    Long blogTotalViewNum();
}
