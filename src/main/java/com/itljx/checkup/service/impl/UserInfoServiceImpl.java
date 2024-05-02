package com.itljx.checkup.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itljx.checkup.entity.UserInfo;
import com.itljx.checkup.mapper.UserInfoMapper;
import com.itljx.checkup.service.UserInfoService;
import org.springframework.stereotype.Service;

@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {
}
