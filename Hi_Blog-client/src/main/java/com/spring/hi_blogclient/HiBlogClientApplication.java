package com.spring.hi_blogclient;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@MapperScan("com.spring.framework.mapper")
@ComponentScan(basePackages = {"com.spring.hi_blogclient", "com.spring.framework"})
public class HiBlogClientApplication {
@Autowired
    PasswordEncoder passwordEncoder;
    public static void main(String[] args) {
        SpringApplication.run(HiBlogClientApplication.class, args);

        // 创建 PasswordEncoder 实例
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        // 要加密的明文密码
        String rawPassword = "root";

        // 加密密码
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // 打印加密后的密码
        System.out.println("Raw Password: " + rawPassword);
        System.out.println("Encoded Password: " + encodedPassword);

    }

}
