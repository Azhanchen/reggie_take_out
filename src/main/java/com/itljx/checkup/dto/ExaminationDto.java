package com.itljx.checkup.dto;

import com.itljx.checkup.entity.Examination;
import com.itljx.checkup.entity.ExaminationFlavor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ExaminationDto extends Examination {

    private List<ExaminationFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
