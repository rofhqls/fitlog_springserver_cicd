package com.fastcam.spserver.repository;

import com.fastcam.spserver.entity.WeightLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface WeightLogRepository extends JpaRepository<WeightLog, Integer> {
    List<WeightLog> findByMemberNum(int num);

    WeightLog findTopByMember_NumOrderByIndateDesc(int id);

    List<WeightLog> findByMemberNumAndIndateBetweenOrderByIndateDesc(int id, Timestamp start, Timestamp end);

    void deleteByMemberNum(int mnum);
}
