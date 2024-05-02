package com.itljx.checkup.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itljx.checkup.dto.SetmealDto;
import com.itljx.checkup.entity.Info;
import com.itljx.checkup.entity.Setmeal;

import java.util.List;

public interface InfoService extends IService<Info> {
    public void saveWithUserInfo(Info info);

    void removeWithUserInfo(List<Long> ids);
}
