package com.spring.framework.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spring.framework.domain.entity.Dto.LoginDto;

public interface LoginDtoMapper extends BaseMapper<LoginDto> {
    LoginDto selectByUsername(String username);
}
