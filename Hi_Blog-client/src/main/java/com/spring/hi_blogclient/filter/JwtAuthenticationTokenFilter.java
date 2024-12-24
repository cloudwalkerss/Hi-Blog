package com.spring.hi_blogclient.filter;

import com.alibaba.fastjson2.JSON;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.spring.framework.domain.ResponseResult;
import com.spring.framework.domain.entity.User;
import com.spring.framework.domain.entity.Vo.LoginVo;
import com.spring.framework.domain.enums.AppHttpCodeEnum;
import com.spring.framework.domain.utils.BeanCopyUtils;
import com.spring.framework.domain.utils.JwtUtils;
import com.spring.framework.domain.utils.WebUtils;
import com.spring.framework.mapper.UserMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
@Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    JwtUtils jwtUtils;

   @Autowired
    UserMapper userMapper;

    @Override
protected void doFilterInternal(HttpServletRequest request,
                                HttpServletResponse response, FilterChain filterChain) throws ServletException,
        IOException {
    //获取请求头中的token
    String jwt = request.getHeader("token");
     String token=jwtUtils.convertToken(jwt);

    //如果token为空，直接放行
    if (StringUtils.isEmpty(token)) {
        filterChain.doFilter(request, response);
        return;
    }
    //解析token
        DecodedJWT claim=null;
    try {
        claim = jwtUtils.resolveJwt(token);
       //当token超时或者token被篡改时会抛出异常
    } catch (Exception e) {
        e.printStackTrace();
        //响应给前端，要求重新登录

        ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        WebUtils.renderString(response, JSON.toJSONString(result));
        return;
    }

    String userId=claim.getId();
        User user = userMapper.selectById(userId);
        LoginVo loginUser= BeanCopyUtils.copyBean(user, LoginVo.class);
        //可能用户信息获取不到，需要判断
    if (Objects.isNull(loginUser)) {
        //响应给前端，要求重新登录

        ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        WebUtils.renderString(response,JSON.toJSONString(result));
        return;
    }
    //存入SecurityContext中代表已经通过验证了
    SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(loginUser, null, null));
    filterChain.doFilter(request, response);




}
}