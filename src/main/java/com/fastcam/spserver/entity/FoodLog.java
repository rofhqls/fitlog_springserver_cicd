package com.fastcam.spserver.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
public class FoodLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int num;

    private String meal_time;
    private String menu;
    private int amount;
    private float carbs;
    private float protein;
    private float fat;
    private int calories;

    private LocalDate indate;

    @ManyToOne
    @JoinColumn(name = "mnum")
    private Member member;

}
