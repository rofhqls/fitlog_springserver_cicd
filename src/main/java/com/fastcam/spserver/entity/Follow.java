package com.fastcam.spserver.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int num;
    private int ffrom;
    private int fto;
}