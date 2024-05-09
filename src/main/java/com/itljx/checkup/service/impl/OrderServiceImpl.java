package com.itljx.checkup.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itljx.checkup.common.BaseContext;
import com.itljx.checkup.common.CustomException;
import com.itljx.checkup.entity.*;
import com.itljx.checkup.mapper.OrderMapper;
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
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders> implements OrderService {
    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private UserService userService;

    @Autowired
    private AddressBookService addressBookService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Transactional
    public void submit(Orders orders) {
        //获得当前用户id
        Long currentId = BaseContext.getCurrentId();
        //查询当前用户的购物车数据
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(currentId!=null,ShoppingCart::getUserId, currentId);
        List<ShoppingCart> shoppingCartList = shoppingCartService.list(queryWrapper);

//        if (shoppingCartList == null || shoppingCartList.size() == 0) {
        if (shoppingCartList.isEmpty()) {
            throw new CustomException("购物车无数据");
        }


        Long addressBookId = orders.getAddressBookId();
        AddressBook addressBook = addressBookService.getById(addressBookId);

        if (addressBook == null) {
            throw new CustomException("地址信息有误，不能下单");
        }
        User user = userService.getById(currentId);

        //向订单表插入数据，一条数据
        AtomicInteger amount = new AtomicInteger(0);
        long orderId = IdWorker.getId();

        //遍历购物车数据 计算金额
        List<OrderDetail> orderDetails = shoppingCartList.stream().map((item) -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setNumber(item.getNumber());
            orderDetail.setExaminationFlavor(item.getExaminationFlavor());
            orderDetail.setExaminationId(item.getExaminationId());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setName(item.getName());
            orderDetail.setImage(item.getImage());
            orderDetail.setAmount(item.getAmount());//单份金额
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());//总金额
            return orderDetail;
        }).collect(Collectors.toList());
        //订单号
        orders.setId(orderId);
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setStatus(2);//待派送
        orders.setAmount(new BigDecimal(amount.get()));//订单金额
        orders.setUserId(currentId);
        orders.setNumber(String.valueOf(orderId));
        orders.setUserName(user.getName());
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());
//        orders.setAddress((addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName())
//                + (addressBook.getCityName() == null ? "" : addressBook.getCityName())
//                + (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
//                + (addressBook.getDetail() == null ? "" : addressBook.getDetail()));//地址详细信息
        orders.setAddress((addressBook.getDetail() == null ? "" : addressBook.getDetail()));//地址详细信息
        this.save(orders);
        //向订单明细表插入数据，可能多条
        orderDetailService.saveBatch(orderDetails);
        //清空购物车
        shoppingCartService.remove(queryWrapper);
    }
}
