package com.itljx.checkup.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itljx.checkup.common.BaseContext;
import com.itljx.checkup.common.R;
import com.itljx.checkup.entity.ShoppingCart;
import com.itljx.checkup.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
@Slf4j
@Service
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart) {
        log.info(String.valueOf(shoppingCart));
        //设置用户id，确认购物车
        shoppingCart.setUserId(BaseContext.getCurrentId());
        //查询当前选择菜品或者套餐是否已经在购物车中
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, shoppingCart.getUserId());
        Long examinationId = shoppingCart.getExaminationId();
        if (examinationId != null) {
            //菜品
            queryWrapper.eq(ShoppingCart::getExaminationId, shoppingCart.getExaminationId());
        } else {
            //套餐
            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }
        ShoppingCart one = shoppingCartService.getOne(queryWrapper);
        //如果存在则数量加一
        if (one != null) {
            Integer number = one.getNumber();
            one.setNumber(number + 1);
            shoppingCartService.updateById(one);
        } else {
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            one = shoppingCart;
        }
        //否则数量为一

        return R.success(one);
    }


    @PostMapping("/sub")
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart) {
//        log.info(String.valueOf(shoppingCart));
        Long currentId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, currentId);
        Long dishId = shoppingCart.getExaminationId();
        if (dishId != null) {
            queryWrapper.eq(ShoppingCart::getExaminationId, shoppingCart.getExaminationId());
        } else {
            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }
        ShoppingCart one = shoppingCartService.getOne(queryWrapper);
        Integer number = one.getNumber();
        if (one != null) {
//            数量不为零
            if (one.getNumber() > 1) {
                one.setNumber(number - 1);
                shoppingCartService.updateById(one);
            } else {
                one.setNumber(number - 1);
                shoppingCartService.updateById(one);
                shoppingCartService.remove(queryWrapper);
            }
            return R.success(one);
        } else {
            return null;
        }
    }


    /**
     * 查看购物车信息
     *
     * @param shoppingCart
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list() {
        log.info("查看购物车");
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);
        List<ShoppingCart> shoppingCartList = shoppingCartService.list(queryWrapper);
        return R.success(shoppingCartList);
    }

    @DeleteMapping("/clean")
    public R<String> clean() {
        log.info("清空购物车");
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        shoppingCartService.remove(queryWrapper);
        return R.success("清空购物车成功");
    }
}
