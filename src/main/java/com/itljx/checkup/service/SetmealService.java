package com.itljx.checkup.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itljx.checkup.dto.SetmealDto;
import com.itljx.checkup.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    public void saveWithDish(SetmealDto setmealDto);

    public void removeWithDish(List<Long> ids);

    SetmealDto getDate(Long id);
}
