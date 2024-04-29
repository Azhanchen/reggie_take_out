package com.itljx.checkup.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itljx.checkup.entity.ShoppingCart;
import com.itljx.checkup.mapper.ShoppingCartMapper;
import com.itljx.checkup.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
