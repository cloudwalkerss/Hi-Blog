package com.spring.framework.service.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spring.framework.domain.entity.Dto.LoginDto;
import com.spring.framework.domain.entity.User;
import com.spring.framework.domain.utils.BeanCopyUtils;
import com.spring.framework.mapper.LoginDtoMapper;
import com.spring.framework.mapper.UserMapper;
import com.spring.framework.service.LoginUserService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class LoginUserImpl extends ServiceImpl<LoginDtoMapper, LoginDto> implements LoginUserService {
     @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername( String username) throws UsernameNotFoundException {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        LoginDto loginDto= BeanCopyUtils.copyBean(user, LoginDto.class);
        loginDto.setPassword(passwordEncoder.encode(user.getPassword()));

        return loginDto;
    }

}
