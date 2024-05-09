package com.itljx.checkup.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itljx.checkup.common.R;
import com.itljx.checkup.dto.ExaminationDto;
import com.itljx.checkup.entity.Category;
import com.itljx.checkup.entity.Examination;
import com.itljx.checkup.entity.ExaminationFlavor;
import com.itljx.checkup.service.CategoryService;
import com.itljx.checkup.service.ExaminationFlavorService;
import com.itljx.checkup.service.ExaminationService;
import com.itljx.checkup.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 菜品管理
 */
@RestController
@RequestMapping("/dish")
@Slf4j
public class ExaminationController {
    @Autowired
    private ExaminationService examinationService;
    @Autowired
    private ExaminationFlavorService examinationFlavorService;
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisTemplate redisTemplate;

    @DeleteMapping
    public R<String> delete(Long ids) {
        examinationService.remove(ids);
        Set keys = redisTemplate.keys("dish_*");
        redisTemplate.delete(keys);
        return R.success("删除成功");
    }



    /**
     * 新增菜品
     *
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody ExaminationDto dishDto) {
        log.info(dishDto.toString());
        examinationService.saveWithFlavor(dishDto);
        //清理某个分类下的菜品缓存
        String key="dish_"+dishDto.getCategoryId()+"_1";
        redisTemplate.delete(key);
        return R.success("新增菜品成功");
    }

    /**
     * 菜品信息分页
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        //构造分页构造器
        Page<Examination> pageInfo = new Page<>(page, pageSize);
        Page<ExaminationDto> dishDtoPage = new Page<>();

        //条件构造器
        LambdaQueryWrapper<Examination> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.like(name != null, Examination::getName, name);
        //添加排序条件
        queryWrapper.orderByDesc(Examination::getUpdateTime);
        //执行分页查询
        examinationService.page(pageInfo, queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");
        List<Examination> records = pageInfo.getRecords();
        List<ExaminationDto> list = records.stream().map((item) -> {
            ExaminationDto dishDto = new ExaminationDto();
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();//分类id
            Category category = categoryService.getById(categoryId);
            String categoryName = category.getName();
            dishDto.setCategoryName(categoryName);
            return dishDto;
        }).collect(Collectors.toList());
        dishDtoPage.setRecords(list);
        return R.success(dishDtoPage);
    }

    /**
     * 根据id查询菜品信息和对应口味信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<ExaminationDto> get(@PathVariable Long id) {
        ExaminationDto dishDto = examinationService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }

    /**
     * 修改菜品
     */
    @PutMapping
    public R<String> update(@RequestBody ExaminationDto dishDto) {
        log.info(dishDto.toString());
        examinationService.updateWithFlavor(dishDto);
        //清理某个分类下的菜品缓存
        String key="dish_"+dishDto.getCategoryId()+"_1";
        redisTemplate.delete(key);

        return R.success("修改菜品成功");
    }

    /**
     * 停售/启售菜品
     */
    @PostMapping("/status/{status}")
    public R<String> updateStatus(@PathVariable Integer status, Long[] ids) {
        List<Long> list = Arrays.asList(ids);
        //构建条件构造器
        LambdaUpdateWrapper<Examination> updateWrapper = new LambdaUpdateWrapper<>();
        //添加过滤器
        updateWrapper.set(Examination::getStatus, status).in(Examination::getId, list);
        //调用
        examinationService.update(updateWrapper);
        //清理所有菜品缓存数据
        Set keys = redisTemplate.keys("dish_*");
        redisTemplate.delete(keys);
        return R.success("修改套餐成功");
    }

    /**
     * 根据条件查询相应的菜品数据
     *
     * @param examination
     * @return
     */
    @GetMapping("/list")
    public R<List<ExaminationDto>> list(Examination examination) {
        List<ExaminationDto> dishDtoList=null;
        String key="dish_"+ examination.getCategoryId()+"_"+ examination.getStatus();
        //先从redis中获取缓存数据
        dishDtoList = (List<ExaminationDto>) redisTemplate.opsForValue().get(key);

        //如果存在，直接返回，无需查询数据库
        if(dishDtoList!=null){
            log.info("查询成功");
            return R.success(dishDtoList);
        }
        //构建查询条件
        LambdaQueryWrapper<Examination> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(examination.getCategoryId() != null, Examination::getCategoryId, examination.getCategoryId());
        queryWrapper.eq(Examination::getStatus, 1);
        //添加排序条件
        queryWrapper.orderByAsc(Examination::getSort).orderByDesc(Examination::getUpdateTime);
        List<Examination> list = examinationService.list(queryWrapper);
        dishDtoList = list.stream().map((item) -> {
            ExaminationDto dishDto = new ExaminationDto();
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();//分类id
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            Long dishId = item.getId();//当前菜品id
            LambdaQueryWrapper<ExaminationFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(ExaminationFlavor::getExaminationId, dishId);
            List<ExaminationFlavor> examinationFlavorList = examinationFlavorService.list(lambdaQueryWrapper);
            dishDto.setFlavors(examinationFlavorList);
            return dishDto;
        }).collect(Collectors.toList());
        //如果不存在，需要查询数据库，将查询到的菜品缓存到Redis
        redisTemplate.opsForValue().set(key,dishDtoList,60, TimeUnit.MINUTES);
        return R.success(dishDtoList);
    }
}
