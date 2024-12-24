package com.spring.framework.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spring.framework.domain.ResponseResult;
import com.spring.framework.domain.entity.Dto.LoginDto;
import com.spring.framework.domain.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2024-12-19 20:23:53
 */

public interface UserService extends IService<User> {

    ResponseResult login( LoginDto loginDto);
}
