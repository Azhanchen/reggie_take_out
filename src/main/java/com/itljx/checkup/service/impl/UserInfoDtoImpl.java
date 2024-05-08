package com.itljx.checkup.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itljx.checkup.common.BaseContext;
import com.itljx.checkup.common.CustomException;
import com.itljx.checkup.dto.UserInfoDto;
import com.itljx.checkup.entity.*;
import com.itljx.checkup.mapper.OrderMapper;
import com.itljx.checkup.mapper.UserInfoDtoMapper;
import com.itljx.checkup.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class UserInfoDtoImpl extends ServiceImpl<UserInfoDtoMapper, UserInfoDto> implements UserInfoDtoService {

}
