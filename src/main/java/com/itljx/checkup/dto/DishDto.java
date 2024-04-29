package com.itljx.checkup.dto;

import com.itljx.checkup.entity.Dish;
import com.itljx.checkup.entity.DishFlavor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
