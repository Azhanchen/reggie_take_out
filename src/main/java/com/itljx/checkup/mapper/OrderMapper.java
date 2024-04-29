package com.itljx.checkup.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itljx.checkup.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Orders> {
}
