package com.spring.hi_blogclient.controller;

import com.spring.framework.domain.ResponseResult;
import com.spring.framework.domain.entity.Dto.LoginDto;
import com.spring.framework.service.UserService;
import com.spring.framework.service.impl.UserServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
   @Autowired
    UserService userService;



}
