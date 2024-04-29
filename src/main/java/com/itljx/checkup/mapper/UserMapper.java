package com.itljx.checkup.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itljx.checkup.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
