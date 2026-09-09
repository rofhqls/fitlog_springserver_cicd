package com.fastcam.spserver.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data

public class Likes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int num;

    @ManyToOne
    @JoinColumn(name = "cnum")
    private Community community;

    @ManyToOne
    @JoinColumn(name = "mnum")
    private Member member;
}
