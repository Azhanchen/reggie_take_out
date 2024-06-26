package com.itljx.checkup.controller;

import com.itljx.checkup.service.OrderDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orderDetail")
@Service
@Slf4j
public class OrderDetailController {
    private OrderDetailService orderDetailService;
}
