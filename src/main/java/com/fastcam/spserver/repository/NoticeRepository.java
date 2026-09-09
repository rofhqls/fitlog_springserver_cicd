package com.fastcam.spserver.repository;

import com.fastcam.spserver.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Integer> {
    Optional<Notice> findByNum(int num);
}
