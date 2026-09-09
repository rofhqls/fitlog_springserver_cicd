package com.fastcam.spserver.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.*;

import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int num;

    @Column(nullable = false)
    private LocalDate subStart;
    @Column(nullable = false)
    private LocalDate subEnd;

    @ManyToOne
    @JoinColumn(name = "mnum")
    private Member member;
}
