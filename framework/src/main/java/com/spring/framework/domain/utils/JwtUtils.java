package com.spring.framework.domain.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.spring.framework.domain.Constants.Const;
import com.spring.framework.domain.entity.Vo.LoginVo;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtils {
    @Value("${spring.security.jwt.key}")
    private String KEY;

    @Value("${spring.security.jwt.expire}")
    private int EXPIRE;

   @Autowired
     StringRedisTemplate template;
    //DecodedJWT 是 auth0 库中的一个类，用于表示解码后的 JWT（JSON Web Token）。
    // 它包含了从 JWT 中解析出的各种信息，如声明（claims）、签发时间、过期时间等。
    // 通过使用 DecodedJWT，你可以方便地访问和验证 JWT 中包含的数据。
    public  DecodedJWT resolveJwt(String headertoken){//从请求头中获取headertoken
         String token = convertToken(headertoken);//去掉Bearer并且判断是否为空

        if(token==null) {
            return null;
        }

        Algorithm algorithm=Algorithm.HMAC256(KEY);
        JWTVerifier verifier= JWT.require(algorithm).build();
        //生成一个验证器
        try {
            //对token进行验证
            DecodedJWT jwt=verifier.verify(token);//是否合法
            //判断这个token是否在黑名单中
           if(isInvalidToken(jwt.getId())) {
               //判断是否在黑名单中这个token
               System.out.println("token在黑名单中");
                return null;
           }

            Date expire=jwt.getExpiresAt();//获取过期时间
            System.out.println("过期时间:"+expire);
            if(expire.before(new Date())){//如果过期时间在当前时间之前
                System.out.println("token已经过期");
                return null;
            }
            return jwt;
        }catch (JWTVerificationException e){
            System.out.println("token发生异常");
            return null;
        }


    }
    //判断是否在黑名单中
    public  boolean isInvalidToken(String jwtId){
      //如果黑名单中存在这个id就代表在
        return template.hasKey(Const.JWT_BLACK_LIST + jwtId);

    }

    private  boolean deletetoken(String id, Date expiresAt) {
        //这里是将token放入黑名单中
        long expire=expiresAt.getTime()-System.currentTimeMillis();
        if(expire>0){
            //已经过期不用拉黑了
            return true;
        }
        template.opsForValue().set(Const.JWT_BLACK_LIST+id,"",expire, TimeUnit.MILLISECONDS);
        return true;
    }


    public String createJwt(LoginVo loginVo, Long id, String username){//待会从userDetails中获取用户的信息

        //加密算法
        Algorithm algorithm=Algorithm.HMAC256(KEY);//秘钥写在配置文件中


        //设置过期时间
        Date expire=expireTime();
        System.out.println("过期时间:"+expire);
        //创建Jwt令牌，并且携带上用户的信息
        //这里的withClaim是设置用户的信息，withExpiresAt是设置过期时间，withIssuedAt是设置签发时间
        return JWT.create().withClaim("username",username)
                .withClaim("id",id)
                .withClaim("nickname",loginVo.getNickName())
                .withClaim("email",loginVo.getEmail())
                .withClaim("avatar",loginVo.getAvatar())
                .withClaim("token",loginVo.getToken())
                .withJWTId(UUID.randomUUID().toString())
                .withExpiresAt(expire)
                .withIssuedAt(new Date()).sign(algorithm);//最后第二个是token的签发时间
                      //以及加密算法的设置
    }

   public Date expireTime(){
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.DATE,EXPIRE);//七天过期

        return calendar.getTime();
    }

    public  String convertToken(String headertoken){
       if(headertoken == null || !headertoken.startsWith("Bearer ")){
           return null;
       }
       return headertoken.substring(7);//去掉Bearer
    }



}
