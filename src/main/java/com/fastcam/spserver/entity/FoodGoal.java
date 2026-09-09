package com.fastcam.spserver.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class FoodGoal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int num;
    @Column(nullable = false)
    private int goalCalories;
    private float goalCarbs;
    private float goalProtein;
    private float goalFat;

    @ManyToOne
    @JoinColumn(name = "mnum")
    private Member member;
}
