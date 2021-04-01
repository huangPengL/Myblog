package com.hpl.blog.dao;

import com.hpl.blog.po.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {

    //找到博客的父级评论
    List<Comment> findByBlogIdAndParentCommentNull(Long blogId,Sort sort);
}
