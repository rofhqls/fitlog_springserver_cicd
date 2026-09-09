package com.fastcam.spserver.entity;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Data
public class    WeightLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int num;

    private float weight;

    private Timestamp indate;

    @ManyToOne
    @JoinColumn(name = "mnum")
    private Member member;

}
