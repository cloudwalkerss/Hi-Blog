package com.spring.framework.service.impl;
 import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
 import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
 import com.spring.framework.domain.ResponseResult;
 import com.spring.framework.domain.entity.Dto.LoginDto;
 import com.spring.framework.domain.entity.User;
 import com.spring.framework.domain.entity.Vo.LoginVo;
 import com.spring.framework.domain.enums.AppHttpCodeEnum;
 import com.spring.framework.mapper.UserMapper;
 import com.spring.framework.service.UserService;
 import org.springframework.beans.BeanUtils;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.security.core.userdetails.UsernameNotFoundException;
 import org.springframework.stereotype.Service;
 import org.springframework.web.bind.annotation.RequestBody;

 import java.util.HashMap;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2024-12-19 20:23:53
 */
 @Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService  {
   @Autowired
    private UserMapper userMapper;

//  @Override
//  //这个方法是spring security的方法，用来查询用户是否存在
//  public LoginVo loadUserByUsername(String username) throws UsernameNotFoundException {
//      //去数据库查询用户是否存在
//   LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper();
//
//    wrapper.eq(User::getUserName, username);
//    User user = userMapper.selectOne(wrapper);
//    if (user == null) {
//      throw new UsernameNotFoundException("用户名或密码错误");
//    }
//
//      LoginVo loginVo = new LoginVo();
//      BeanUtils.copyProperties(user, loginVo);
//      return loginVo;
//    return org.springframework.security.core.userdetails.User.withUsername(username)
//            .password(user.getPassword())
//
//            .roles(user.getType())
//            .build();
     //返回的这个数据会交给spring security去验证，如果验证通过，就会生成authentication对象

 // }

     @Override
     public ResponseResult login(@RequestBody LoginDto loginDto) {
          String password=loginDto.getPassword();
          User user=userMapper.selectByUsername(loginDto.getUsername());
          if(user==null){
              return ResponseResult.errorResult(AppHttpCodeEnum.USERNAME_NOT_EXIST);
          }
            if(!user.getPassword().equals(password)){
                return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_ERROR);
            }

         return ResponseResult.okResult();



     }
 }
