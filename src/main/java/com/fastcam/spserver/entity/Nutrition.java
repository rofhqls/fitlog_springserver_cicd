package com.fastcam.spserver.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Data
public class Nutrition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int num;

    @Column(nullable = false)
    private String fname;

    @Column(nullable = false)
    private int unit;

    @Column(nullable = false)
    private int kcal;

    private float carbs;
    private float protein;
    private float fat;

}
