package com.itljx.checkup.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itljx.checkup.entity.User;

public interface UserService extends IService<User> {
    //发邮件
    void sendMsg(String to, String subject, String text);
}
