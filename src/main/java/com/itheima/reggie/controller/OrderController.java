package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.OrdersDto;
import com.itheima.reggie.entity.OrderDetail;
import com.itheima.reggie.entity.Orders;
import com.itheima.reggie.service.OrderDetailService;
import com.itheima.reggie.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order")
@Service
@Slf4j
public class OrderController {
    @Autowired
    private OrderService ordersService;

    @Autowired
    private OrderDetailService orderDetailService;


    /**
     * 用户下单
     *
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders) {
        log.info(String.valueOf(orders));
        ordersService.submit(orders);
        return R.success("下单成功");
    }

    @GetMapping("/userPage")
    public R<Page> page(int page, int pageSize) {
        Page<Orders> pageInfo = new Page<>(page, pageSize);
        Page<OrdersDto> dtoPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getUserId, BaseContext.getCurrentId());
        queryWrapper.orderByAsc(Orders::getOrderTime);
        ordersService.page(pageInfo, queryWrapper);
        List<Orders> records = pageInfo.getRecords();
        List<OrdersDto> ordersDtoList = records.stream().map((item) -> {
            OrdersDto ordersDto = new OrdersDto();
            //根据id获取订单明细
            List<OrderDetail> orderDetails = orderDetailService.getOrderDetailById(item.getId());
            ordersDto.setOrderDetails(orderDetails);
            BeanUtils.copyProperties(item, ordersDto);
            return ordersDto;
        }).collect(Collectors.toList());
        BeanUtils.copyProperties(pageInfo, dtoPage);
        dtoPage.setRecords(ordersDtoList);
        return R.success(dtoPage);
    }

    @GetMapping("/page")
    public R<Page> pageBackend(int page, int pageSize, String number, String beginTime, String endTime) {
        Page<Orders> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(number != null, Orders::getNumber, number)
                .ge(StringUtils.isNotEmpty(beginTime), Orders::getOrderTime, beginTime)
                .le(StringUtils.isNotEmpty(endTime), Orders::getOrderTime, endTime);
        queryWrapper.orderByAsc(Orders::getOrderTime);
        ordersService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 派送订单
     *
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody Map<String, String> map) {
        String id = map.get("id");
        Orders orders = ordersService.getById(id);
        String status = map.get("status");
        LambdaUpdateWrapper<Orders> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Orders::getNumber, id);
        updateWrapper.set(Orders::getStatus, status);
        ordersService.update(updateWrapper);
        return R.success("修改状态成功");
    }
}
