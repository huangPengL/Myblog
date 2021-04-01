package com.hpl.blog.service;

import com.hpl.blog.po.User;

public interface UserService {
    User checkUser(String username,String password);
}
