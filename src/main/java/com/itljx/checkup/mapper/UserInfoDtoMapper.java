package com.itljx.checkup.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itljx.checkup.dto.UserInfoDto;
import com.itljx.checkup.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserInfoDtoMapper extends BaseMapper<UserInfoDto> {
}
