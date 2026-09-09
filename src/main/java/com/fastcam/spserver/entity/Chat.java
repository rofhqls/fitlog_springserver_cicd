package com.fastcam.spserver.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Data
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int num;

    private String sender;

    @Column(nullable = false, length = 1000)
    private String content;

    @CreationTimestamp
    @Column(columnDefinition = "datetime default now()")
    private Timestamp indate;

    @ManyToOne
    @JoinColumn(name = "mnum")
    private Member member;

}
