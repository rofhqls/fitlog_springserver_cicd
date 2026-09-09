package com.fastcam.spserver.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class MemberRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int num;

    @Column(nullable = false)
    private String roleName;

    @ManyToOne
    @JoinColumn(name = "mnum")
    private Member member;
}
