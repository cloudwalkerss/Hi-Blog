package com.spring.framework.listener;

import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(queues = "mail")
public class MailQueueListener {
    @Resource
    JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    String username;

    @RabbitHandler
    public void sendMail(Map<String, Object> data) {
        // 发送邮件
        // 1. 设置邮件发送信息
        // 2. 发送邮件
        String email = (String) data.get("email");
        Integer code = (Integer) data.get("code");
        String type = (String) data.get("type");
        String ip = (String) data.get("ip");

        SimpleMailMessage message=switch (type){
            case "register" -> createMail("欢迎注册我们的网站", "您的验证码是" + code+",有限时间三分钟" +
                    "，为了保障您的账号安全，请妥善保管", email);
            case "reset" -> createMail("重置密码", "您好，您正在进行密码重置" +
                    "您的验证码是" + code, email);
            default -> null;
        };
        if(message==null){
            return;
        }
        mailSender.send(message);
        }
    //参数分别是标题，内容，接收者
    public SimpleMailMessage createMail(String title, String content, String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(title);
        message.setText(content);
        message.setTo(email);//发送给谁
        message.setFrom(username);
        return  message;
    }
    }


