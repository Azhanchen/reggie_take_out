package com.itljx.checkup.dto;

import com.itljx.checkup.entity.Setmeal;
import com.itljx.checkup.entity.SetmealExamination;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealExamination> setmealExaminations;

    private String categoryName;
}
