package com.itljx.checkup.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.itljx.checkup.entity.OrderDetail;

import java.util.List;

public interface OrderDetailService extends IService<OrderDetail> {
    List<OrderDetail> getOrderDetailById(Long id);
}
