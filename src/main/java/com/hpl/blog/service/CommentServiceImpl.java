package com.hpl.blog.service;


import com.hpl.blog.dao.CommentRepository;
import com.hpl.blog.po.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;


    /**
     * 保存评论信息
     * @param comment
     * @return
     */
    @Transactional
    @Override
    public Comment saveComment(Comment comment) {
        //1、若当前的评论是针对其他用户的回复(parentCommentId != -1)，则设置将当前评论的父级评论
        Long parentCommentId = comment.getParentComment().getId();
        if(parentCommentId != -1){
            comment.setParentComment(commentRepository.findById(parentCommentId).get());
        }
        else{
            comment.setParentComment(null);
        }

        //2、保存到数据库
        return commentRepository.save(comment);
    }

    /**
     * 获取某篇博客的评论信息
     * @param blogId
     * @return
     */
    @Override
    public List<Comment> listCommentByBlogIdAndParentCommentNull(Long blogId) {
        Sort sort = Sort.by(Sort.Direction.ASC,"createTime");
        return commentRepository.findByBlogIdAndParentCommentNull(blogId,sort);
    }


    @Override
    public Long commentCount() {
        return commentRepository.count();
    }
}
