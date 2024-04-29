package com.itljx.checkup.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.itljx.checkup.entity.Orders;

public interface OrderService extends IService<Orders> {
    public void submit(Orders orders);
}
