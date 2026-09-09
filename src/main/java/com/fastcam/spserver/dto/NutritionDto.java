package com.fastcam.spserver.dto;

import lombok.Data;

@Data
public class NutritionDto {

    private String fname;
    private int unit;
    private int kcal;
    private float carbs;
    private float protein;
    private float fat;
}
