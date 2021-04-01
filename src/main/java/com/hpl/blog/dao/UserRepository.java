package com.hpl.blog.dao;

import com.hpl.blog.po.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {

    //由于JPA没有我们需要的业务查询方法，但是只要我们创建遵循JPA的命名规则的方法即可查询
    User findByUsernameAndPassword(String username,String password);
}
