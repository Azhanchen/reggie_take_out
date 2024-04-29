package com.itljx.checkup.dto;

import com.itljx.checkup.entity.Setmeal;
import com.itljx.checkup.entity.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
