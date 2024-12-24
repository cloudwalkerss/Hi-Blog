package com.spring.framework.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spring.framework.domain.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 用户表(User)表数据库访问层
 *
 * @author makejava
 * @since 2024-12-19 20:23:53
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("select * from hh_user where user_Name=#{username}")
    User selectByUsername(String username);
}
