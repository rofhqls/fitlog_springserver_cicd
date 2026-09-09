package com.fastcam.spserver.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Data
public class Qna {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int num;
    @Column(nullable = false)
    private String subject;
    @Column(nullable = false, length = 1000)
    private String content;
    @CreationTimestamp
    @Column(columnDefinition = "datetime default now()")
    private Timestamp indate;
    @Column(length = 1000)
    private String reply;

    @ManyToOne
    @JoinColumn(name = "mnum")
    private Member member;
}
