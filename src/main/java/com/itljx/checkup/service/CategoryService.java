package com.itljx.checkup.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itljx.checkup.entity.Category;


public interface CategoryService extends IService<Category> {
    public void remove(Long ids);
}
