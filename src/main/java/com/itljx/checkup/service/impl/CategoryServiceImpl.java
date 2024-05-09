package com.itljx.checkup.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itljx.checkup.common.CustomException;
import com.itljx.checkup.entity.Category;
import com.itljx.checkup.entity.Examination;
import com.itljx.checkup.entity.Setmeal;
import com.itljx.checkup.mapper.CategoryMapper;
import com.itljx.checkup.service.CategoryService;
import com.itljx.checkup.service.ExaminationService;
import com.itljx.checkup.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private ExaminationService examinationService;//菜品

    @Autowired
    private SetmealService setmealService;//套餐

    /**
     * 根据id删除分类
     *
     * @param id
     */
    @Override
    public void remove(Long ids) {
        LambdaQueryWrapper<Examination> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件，根据id进行查询
        dishLambdaQueryWrapper.eq(Examination::getCategoryId, ids);
        int count1 = examinationService.count(dishLambdaQueryWrapper);
        log.info("关联数：{}", count1);
        //查询当前是否关联了菜品，如果关联抛出业务异常
        if (count1 > 0) {
            throw new CustomException("当前分类下关联了菜品，不能删除");
        }
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, ids);
        int count2 = setmealService.count(setmealLambdaQueryWrapper);
        //查询当前是否关联了套餐，如果关联抛出业务异常
        if (count2 > 0) {
            throw new CustomException("当前分类下关联了套餐，不能删除");
        }
        //正常删除分类
        super.removeById(ids);
    }
}
