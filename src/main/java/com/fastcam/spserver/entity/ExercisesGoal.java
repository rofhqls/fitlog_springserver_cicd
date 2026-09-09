package com.fastcam.spserver.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ExercisesGoal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int num;
    @Column(nullable = false)
    private int goalTime;
    private int goalCalories;
    private float goalWeight;

    @ManyToOne
    @JoinColumn(name = "mnum")
    private Member member;
}
