package com.fastcam.spserver.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.*;

import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@Data

public class ExercisesLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int num;

    @Column(length = 100)
    private String exName;
    private int exerciseTime;
    private int calories;

    private LocalDate indate;

    @ManyToOne
    @JoinColumn(name = "mnum")
    private Member member;

}
