package com.itljx.checkup.dto;

import com.itljx.checkup.entity.OrderDetail;
import com.itljx.checkup.entity.Orders;
import lombok.Data;

import java.util.List;

@Data
public class OrdersDto extends Orders {

    private String userName;

    private String phone;

    private String address;

    private String consignee;

    private List<OrderDetail> orderDetails;

}
