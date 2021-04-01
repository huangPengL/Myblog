package com.hpl.blog.web.front;

import com.hpl.blog.po.Comment;
import com.hpl.blog.po.User;
import com.hpl.blog.service.BlogsService;
import com.hpl.blog.service.CommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/front")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private BlogsService blogsService;

    //初始化头像url为application.properties中定义的资源路径
    @Value("${comment.avatar}")
    private String avatar;


    /**
     * 刷新博客评论区域
     * @param blogId
     * @param model
     * @return
     */
    @GetMapping("/comment/{blogId}")
    public String comment(@PathVariable Long blogId,Model model){
        //找到所有父级评论
        List<Comment> comments = commentService.listCommentByBlogIdAndParentCommentNull(blogId);

        //将父级评论的所有子评论树 “压缩”处理
        List<Comment> restructedComments = restructComments(comments);

        model.addAttribute("comments",restructedComments);
        return "front/blog :: commentList";
    }

    // *将父级评论的所有子评论树 “压缩”处理 *//
    private List<Comment> restructComments(List<Comment> comments){

        //1、将父级评论复制一份
        List<Comment> copyComments = new ArrayList<>();
        for(Comment comment : comments){
            Comment temp = new Comment();
            BeanUtils.copyProperties(comment,temp);
            copyComments.add(temp);
        }

        //2、把父级评论的 多级子评论树 压缩成 一层子评论列表
        for(Comment comment : copyComments){
            //2.1 获取某个父级评论的单层子评论列表
            List<Comment> replyComments = comment.getReplyComments();

            //2.2将该单层子评论列表的所有子评论树的放入列表中
            for(Comment replyComment : replyComments){
                circulateReply(replyComment);
            }
            //2.3 将该列表放入父评论的回复列表中
            comment.setReplyComments(oneLevelreplyComments);

            //2.4 每次 压缩成功，将临时列表清除
            oneLevelreplyComments = new ArrayList<>();
        }

        return copyComments;
    }

    //临时列表，存储一个父评论的所有回复列表
    private List<Comment> oneLevelreplyComments = new ArrayList<>();

    // *将当前回复评论的所有子评论放入到临时列表中 *//
    private void circulateReply(Comment replyComment){
        //1、先将当前的回复放入临时列表中
        oneLevelreplyComments.add(replyComment);

        //2、若当前回复有子回复列表，则遍历并放入临时列表中
        if(replyComment.getReplyComments().size() > 0){
            List<Comment> deep_replyComments = replyComment.getReplyComments();
            for(Comment deep_replyComment : deep_replyComments){
                circulateReply(deep_replyComment);
            }
        }
    }

    /**
     * 前端点击提交评论，处理提交的请求，刷新对应的博客评论
     * @param comment
     * @return
     */
    @PostMapping("/postComment")
    public String postComment(Comment comment, HttpSession session){
        //1、对提交的评论对象进行初始化（设置其中的Blog，avatar,createTime）
        Long blogId = comment.getBlog().getId();
        comment.setBlog(blogsService.getBlog_back(blogId));
        comment.setCreateTime(new Date());

        //1.1 若是管理员提交评论
        User user = (User)session.getAttribute("user");
        if(user != null){
            comment.setAvatar(user.getAvatar());
            comment.setUsertype(true);
        }
        //1.2 若是普通用户提交评论
        else{
            comment.setAvatar(avatar);
            comment.setUsertype(false);
        }

        //2、将提交的评论保存到数据库
        commentService.saveComment(comment);

        //3、刷新博客评论区域
        return "redirect:/front/comment/" + blogId;
    }
}
