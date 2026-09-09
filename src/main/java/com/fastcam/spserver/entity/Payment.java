package com.fastcam.spserver.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigInteger;
import java.sql.Timestamp;

@Entity
@Data
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int num;
    @CreationTimestamp
    @Column(columnDefinition = "datetime default now()")
    private Timestamp indate;
    @Column(nullable = false)
    private Long price;
    @Column(nullable = false)
    private String productName;
    @Column(nullable = false, length = 200)
    private String paymentKey;

    @ManyToOne
    @JoinColumn(name = "mnum")
    private Member member;
}
