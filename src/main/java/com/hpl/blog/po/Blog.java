package com.hpl.blog.po;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Fetch;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Entity
@Data       //不用get set
@ToString   //在编译的时候生成toString
@AllArgsConstructor //全参构造器
@NoArgsConstructor  //无参构造器
@Table(name = "t_blog")
public class Blog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    @Basic(fetch = FetchType.LAZY)
    @Lob
    private String content;

    private String firstPicture;
    private String flag;                         //原创、转载、翻译
    private Integer viewNum;                    //浏览次数
    private boolean appreciationState;        //是否开启 赞赏
    private boolean shareState;                //是否开启 版权声明
    private boolean commentState;              //是否开启 评论
    private boolean publishState;              //当前博客是 发布状态(true)还是草稿状态(false)
    private boolean recommendState;            //是否开启 推荐
    private String description;                 //博客的描述信息

    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;                    //发布博客时间
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;                    //更新博客时间

    @ManyToOne
    private Type type;                                   //多个博客对应一个类别

    @ManyToMany(cascade = {CascadeType.PERSIST})         //当新增一个博客时，有新的标签将会级联新增到标签类中
    private List<Tag> tags = new ArrayList<>();         //多个博客对应多个标签

    @ManyToOne
    private User user;                                   //多个博客对应一个用户

    @OneToMany(mappedBy = "blog")
    private List<Comment> comments = new ArrayList<>(); //一个博客对应多个评论

    @Transient                                            //不需要保存到数据库
    private Long typeId;

    @Transient                                            //不需要保存到数据库
    private String tagIds;

    //将用List存储的tags转化为tagIds，即转化为tagIds = "1,2,3..."
    public void initTags(){
        this.tagIds = tagListTotagIds(this.getTags());
    }
    String tagListTotagIds(List<Tag> tags){
        StringBuffer Ids = new StringBuffer();
        if(tags.isEmpty() == false){
            boolean flag = false;
            for(Tag tag : tags){
                if(flag != false){
                    Ids.append(",");
                }
                else{
                    flag = true;
                }
                Ids.append(tag.getId());
            }
            return Ids.toString();
        }
        else{
            return Ids.toString();
        }
    }
}
