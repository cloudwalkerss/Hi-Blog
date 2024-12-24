package com.spring.framework.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.spring.framework.domain.entity.Dto.LoginDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface LoginUserService extends UserDetailsService, IService<LoginDto> {

}
