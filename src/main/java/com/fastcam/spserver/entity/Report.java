package com.fastcam.spserver.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data

public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int num;

    @Column(nullable = false, length = 200)
    private String content;
    @Column(length = 200)
    private String memo;

    @ManyToOne
    @JoinColumn(name = "mnum")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "cnum")
    private Community community;
}
