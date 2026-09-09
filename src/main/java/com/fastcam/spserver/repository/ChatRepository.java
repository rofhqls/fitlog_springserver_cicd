package com.fastcam.spserver.repository;

import com.fastcam.spserver.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Integer> {
    List<Chat> findByMemberNumOrderByNum(int mnum);

    void deleteByMemberNum(int mnum);
}
