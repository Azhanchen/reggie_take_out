package com.itljx.checkup.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itljx.checkup.entity.User;
import com.itljx.checkup.mapper.UserMapper;
import com.itljx.checkup.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    //把yml配置的邮箱号赋值到form
    @Value("${spring.mail.username}")
    private String form;

    //发送邮件需要的对象
    @Autowired
    private JavaMailSender javaMailSender;

    //邮件发送人
    @Override
    public void sendMsg(String to, String subject, String text) {
        //发送简单邮件，简单有i按不包括附件等别的
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(form);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        //发送邮件
        javaMailSender.send(message);
    }
}
