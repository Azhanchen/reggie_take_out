package com.itljx.checkup.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itljx.checkup.dto.ExaminationDto;
import com.itljx.checkup.entity.Examination;
import com.itljx.checkup.entity.ExaminationFlavor;
import com.itljx.checkup.mapper.ExaminationMapper;
import com.itljx.checkup.service.ExaminationFlavorService;
import com.itljx.checkup.service.ExaminationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ExaminationServiceImpl extends ServiceImpl<ExaminationMapper, Examination> implements ExaminationService {
    @Autowired
    private ExaminationFlavorService examinationFlavorService;

    @Autowired
    private ExaminationService examinationService;

    @Override
    public void remove(Long ids) {
        //正常删除分类
        super.removeById(ids);
    }


    /**
     * 新增菜品同时保存口味数据
     *
     * @param dishDto
     */
    //处理多张表，开启事务处理
    @Transactional
    @Override
    public void saveWithFlavor(ExaminationDto dishDto) {
        //保存菜品基本信息到菜品表dish
        this.save(dishDto);
        Long dishId = dishDto.getId();
        List<ExaminationFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) -> {
            item.setExaminationId(dishId);
            return item;
        }).collect(Collectors.toList());
        //保存口味数据到口味表
        examinationFlavorService.saveBatch(flavors);
    }

    @Override
    public ExaminationDto getByIdWithFlavor(Long id) {
        //查询菜品基本信息 dish表
        Examination examination = this.getById(id);

        //拷贝
        ExaminationDto dishDto = new ExaminationDto();
        BeanUtils.copyProperties(examination, dishDto);

        //查询菜品对应口味信息 dish_flavor表
        LambdaQueryWrapper<ExaminationFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ExaminationFlavor::getExaminationId, examination.getId());
        List<ExaminationFlavor> flavors = examinationFlavorService.list(queryWrapper);
        dishDto.setFlavors(flavors);
        return dishDto;
    }

    @Override
    @Transactional
    public void updateWithFlavor(ExaminationDto dishDto) {
        //更新dish表基本信息
        this.updateById(dishDto);
        //先清理菜品口味数据
        LambdaQueryWrapper<ExaminationFlavor> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(ExaminationFlavor::getExaminationId, dishDto.getId());
        examinationFlavorService.remove(queryWrapper);
        //添加当前提交过来的口味数据
        List<ExaminationFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) -> {
            item.setExaminationId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        examinationFlavorService.saveBatch(flavors);

    }


}
