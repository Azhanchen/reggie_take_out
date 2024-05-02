package com.itljx.checkup.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itljx.checkup.common.R;
import com.itljx.checkup.dto.DishDto;
import com.itljx.checkup.dto.SetmealDto;
import com.itljx.checkup.entity.*;
import com.itljx.checkup.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 套餐关联
 */
@RestController
@RequestMapping("/info")
@Slf4j
public class InfoController {
    @Autowired
    private InfoService infoService;

    @Autowired
    private UserInfoService userInfoService;

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize) {
        //构造分页构造器
        Page<Info> pageInfo = new Page<>(page, pageSize);
        //条件构造器
        LambdaQueryWrapper<Info> queryWrapper = new LambdaQueryWrapper<>();
        //添加排序条件
        queryWrapper.orderByAsc(Info::getSort);
        infoService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    @PostMapping
    @CacheEvict(value = "infoCache",allEntries = true)
    public R<String> save(@RequestBody Info info) {
        infoService.saveWithUserInfo(info);
        return R.success("发布成功");
    }

    @PostMapping("/status/{status}")
    public R<String> updateStatus(@PathVariable Integer status, Long[] ids) {
        List<Long> list = Arrays.asList(ids);
        LambdaUpdateWrapper<Info> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(Info::getStatus, status).in(Info::getId, list);
        infoService.update(updateWrapper);
        return R.success("更新通知状态成功");
    }

    @GetMapping("/{id}")
    public R<Info> queryInfoById(@PathVariable Long id) {
        Info info = infoService.getById(id);
        return R.success(info);
    }

    @PutMapping
    public R<String> edit(@RequestBody Info info){
        if (info==null){
            return R.error("请求异常");
        }
        infoService.updateById(info);
        return R.success("通知修改成功");
    }

    @CacheEvict(value = "infoCache",allEntries = true)
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids) {
        infoService.removeWithUserInfo(ids);
        return R.success("删除成功");
    }
}
