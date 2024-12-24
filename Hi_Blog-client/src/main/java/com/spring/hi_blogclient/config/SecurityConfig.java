package com.spring.hi_blogclient.config;

import com.alibaba.fastjson2.JSON;
import com.spring.framework.domain.ResponseResult;
import com.spring.framework.domain.entity.Dto.LoginDto;
import com.spring.framework.domain.entity.User;
import com.spring.framework.domain.entity.Vo.LoginVo;
import com.spring.framework.domain.utils.BeanCopyUtils;
import com.spring.framework.domain.utils.JwtUtils;
import com.spring.framework.domain.utils.WebUtils;
import com.spring.framework.handler.security.AccessDeniedHandlerImpl;
import com.spring.framework.handler.security.AuthenticationEntryPointImpl;
import com.spring.framework.mapper.UserMapper;
import com.spring.framework.service.impl.UserServiceImpl;
//import com.spring.hi_blogclient.filter.JwtAuthenticationTokenFilter;
import com.spring.hi_blogclient.filter.JwtAuthenticationTokenFilter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Objects;

@Configuration
public class SecurityConfig  {

    @Autowired
    AccessDeniedHandlerImpl accessDeniedHandlerImpl;
    @Autowired
    AuthenticationEntryPointImpl authenticationEntryPointImpl;
    @Autowired
    UserServiceImpl userServiceImpl;
    @Autowired
    JwtUtils jwtUtil;
    @Autowired
    WebUtils webUtils;
    @Autowired
    JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
    @Autowired
    private UserMapper userMapper;

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class).build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(conf -> conf
                        .requestMatchers("/api/auth/**").permitAll()//这里是允许api/auth/所有的请求，也就是登录页面的访问
                        .anyRequest().authenticated()//其他的请求都需要认证
                )
                .formLogin(conf -> conf .loginProcessingUrl("/api/auth/login")//这里是配置登录的接口

                        .successHandler(this::LoginSuccessHandler) //这里是配置登录成功的处理器

                )
                .logout(conf -> conf.logoutUrl("/api/auth/logout")//这里是配置退出的接口
                        .logoutSuccessHandler(this::onLogoutSuccess)//这里是配置退出成功的处理器
                )
                .exceptionHandling(conf ->conf.authenticationEntryPoint(authenticationEntryPointImpl)//这里是配置没登录的处理器
                        .accessDeniedHandler(accessDeniedHandlerImpl)//登录但是未授权的处理器
                )
                .csrf(AbstractHttpConfigurer::disable)//这里是禁用csrf
                .sessionManagement(conf -> conf.sessionCreationPolicy(SessionCreationPolicy.STATELESS))//这里是配置session的管理策略
                //因为前后端分离，所以这里不需要session，所以配置为STATELESS，security不会处理session
                .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class) .build();

    }

    private void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        // 退出登录成功后的处理



        String token = request.getHeader("Authorization");

        ResponseResult responseResult = new ResponseResult();
        if (jwtUtil.resolveJwt(token) == null) {
            //如果token不合法
            WebUtils.renderString(response, JSON.toJSONString(responseResult.error(400, "退出失败")));
        } else {
            //如果token合法
            WebUtils.renderString(response, JSON.toJSONString(responseResult.ok("退出成功")));
        }


    }

    //这里认证通过后，springsecurity会传一个authentication对象过来，这个对象里面包含了用户的信息

    public void LoginSuccessHandler(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        //登录成功后


        LoginDto loginDto= (LoginDto) authentication.getPrincipal();//这里是用户的信息


         User user=userMapper.selectByUsername(loginDto.getUsername());


        if(Objects.isNull(user)){
            //如果token不合法
//            WebUtils.renderString(response, new ResponseResult().error(400, "登录失败").toString());
            WebUtils.renderString(response,"登陆失败");
            return;
        }

        Long id = user.getId();
        LoginVo loginVo = BeanCopyUtils.copyBean(user, LoginVo.class);
        //返回了一个token
        String token = jwtUtil.createJwt(loginVo, id, loginVo.getUsername());//这里是生成token
        loginVo.setToken(token);
        ResponseResult result = new ResponseResult();
//我们需要给他颁发一个token，这样他才能访问我们的资源
        //这里是将token返回给前端

        //加上令牌过期时间
        loginVo.setExpireTime(jwtUtil.expireTime());

        WebUtils.renderString(response, JSON.toJSONString(result.ok(loginVo)));

    }
}
