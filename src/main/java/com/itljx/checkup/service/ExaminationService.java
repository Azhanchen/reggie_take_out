package com.itljx.checkup.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itljx.checkup.dto.ExaminationDto;
import com.itljx.checkup.entity.Examination;

public interface ExaminationService extends IService<Examination> {
    //新增菜品，同时插入菜品对应的口味数据，需要同时操作两张表
    public void saveWithFlavor(ExaminationDto dishDto);

    //根据id查询菜品信息和对应口味信息
    public ExaminationDto getByIdWithFlavor(Long id);

    //更新菜品信息，同时更新口味
    public void updateWithFlavor(ExaminationDto dishDto);


    public void remove(Long ids);
}
