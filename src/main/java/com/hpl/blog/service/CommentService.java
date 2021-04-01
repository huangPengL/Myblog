package com.hpl.blog.service;

import com.hpl.blog.po.Comment;

import java.util.List;

public interface CommentService {

    Comment saveComment(Comment comment);

    List<Comment> listCommentByBlogIdAndParentCommentNull(Long blogId);

    Long commentCount();
}
