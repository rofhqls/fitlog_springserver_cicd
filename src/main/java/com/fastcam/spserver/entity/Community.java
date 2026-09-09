package com.fastcam.spserver.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Data
public class Community {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int num;

    private String title;

    @Column(nullable = false, length = 500)
    private String content;

    private int viewCount;
    private String image;

    @CreationTimestamp
    @Column(columnDefinition = "datetime default now()")
    private Timestamp indate;

    @Column(columnDefinition = "varchar(1) default 'Y'")
    private String status = "Y";

    @ManyToOne
    @JoinColumn(name = "mnum")
    private Member member;
}
