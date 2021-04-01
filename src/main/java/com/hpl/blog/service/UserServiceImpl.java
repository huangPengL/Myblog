package com.hpl.blog.service;

import com.hpl.blog.dao.UserRepository;
import com.hpl.blog.po.User;
import com.hpl.blog.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    //使用DAO的操作，注入
    @Autowired
    private UserRepository userRepository;


    /**
     * 登录后台查询
     * @param username
     * @param password
     * @return
     */
    @Override
    public User checkUser(String username, String password) {

        //采用MD5加密方式
        User user = userRepository.findByUsernameAndPassword(username, MD5Utils.code(password));
        return user;
    }
}
