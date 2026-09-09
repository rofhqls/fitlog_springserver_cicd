package com.fastcam.spserver.entity;


import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;

import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@Data
@DynamicInsert
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int num;
    @Column(nullable = false)
    private String id;
    @Column(length = 100)
    private String snsid;
    private String pass;
    @Column(nullable = false)
    private String name;
    private String phone;
    @Column(length = 100)
    private String email;
    @Column(length = 500)
    private String profileImg;
    @Column(columnDefinition = "varchar(20) default 'LOCAL'")
    private String provider;
}
