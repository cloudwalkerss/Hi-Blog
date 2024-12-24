package com.spring.framework.handler.security;


import com.alibaba.fastjson2.JSON;
import com.spring.framework.domain.ResponseResult;
import com.spring.framework.domain.enums.AppHttpCodeEnum;
import com.spring.framework.domain.utils.WebUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
    public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse
            response, AuthenticationException authException)
            {
                ResponseResult result=null;
                //响应给前端
                if(authException instanceof InsufficientAuthenticationException){
                result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
                }
            else if(authException instanceof BadCredentialsException){
                    result = ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_ERROR.getCode(),authException.getMessage());

                }
                else{
                    result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(),"认证或授权出现问题");
                }
                WebUtils.renderString(response, JSON.toJSONString(result));




            }
    }