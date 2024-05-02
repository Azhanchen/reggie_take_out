package com.itljx.checkup.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itljx.checkup.common.BaseContext;
import com.itljx.checkup.common.CustomException;
import com.itljx.checkup.dto.SetmealDto;
import com.itljx.checkup.entity.*;
import com.itljx.checkup.mapper.InfoMapper;
import com.itljx.checkup.mapper.SetmealMapper;
import com.itljx.checkup.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class InfoServiceImpl extends ServiceImpl<InfoMapper, Info> implements InfoService {
    @Autowired
    private InfoService infoService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private UserService userService;

    @Transactional
    public void saveWithUserInfo(Info info) {
        this.save(info);
        List<User> users =  userService.list();
        UserInfo userInfo=new UserInfo();
        for (User user : users) {
            Long userId = user.getId();
            userInfo.setId(null);
            userInfo.setUserId(userId);
            userInfo.setInfoId(info.getId());
            userInfoService.save(userInfo);
        }
    }

    @Override
    public void removeWithUserInfo(List<Long> ids) {
        LambdaQueryWrapper<Info> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Info::getId, ids);
        queryWrapper.eq(Info::getStatus, 1);
        int count = this.count(queryWrapper);
        if (count > 0) {
            throw new CustomException("通知正开启中，无法删除");
        }
        //先删除套餐表中数据
        this.removeByIds(ids);
        //删除关联表数据
        LambdaQueryWrapper<UserInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(UserInfo::getInfoId, ids);
        userInfoService.remove(lambdaQueryWrapper);
    }
}
