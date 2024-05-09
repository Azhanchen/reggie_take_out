package com.itljx.checkup.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itljx.checkup.common.CustomException;
import com.itljx.checkup.dto.SetmealDto;
import com.itljx.checkup.entity.Setmeal;
import com.itljx.checkup.entity.SetmealExamination;
import com.itljx.checkup.mapper.SetmealMapper;
import com.itljx.checkup.service.SetmealExaminationService;
import com.itljx.checkup.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealExaminationService setmealExaminationService;

    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        this.save(setmealDto);
        List<SetmealExamination> setmealExaminations = setmealDto.getSetmealExaminations();
        setmealExaminations.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        setmealExaminationService.saveBatch(setmealExaminations);
    }

    @Transactional
    public void removeWithDish(List<Long> ids) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId, ids);
        queryWrapper.eq(Setmeal::getStatus, 1);
        int count = this.count(queryWrapper);
        if (count > 0) {
            throw new CustomException("套餐正在售卖中，无法删除");
        }
        //先删除套餐表中数据
        this.removeByIds(ids);
        //删除关联表数据
        LambdaQueryWrapper<SetmealExamination> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SetmealExamination::getSetmealId, ids);
        setmealExaminationService.remove(lambdaQueryWrapper);
    }

    /**
     * 回显套餐数据：根据套餐id查询套餐
     * @return
     */
    @Override
    public SetmealDto getDate(Long id) {
        Setmeal setmeal = this.getById(id);
        SetmealDto setmealDto = new SetmealDto();
        LambdaQueryWrapper<SetmealExamination> queryWrapper = new LambdaQueryWrapper();
        //在关联表中查询，setmealdish
        queryWrapper.eq(id!=null, SetmealExamination::getSetmealId,id);

        if (setmeal != null){
            BeanUtils.copyProperties(setmeal,setmealDto);
            List<SetmealExamination> list = setmealExaminationService.list(queryWrapper);
            setmealDto.setSetmealExaminations(list);
            return setmealDto;
        }
        return null;
    }
}
