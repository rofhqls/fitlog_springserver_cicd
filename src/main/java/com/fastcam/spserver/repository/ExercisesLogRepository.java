package com.fastcam.spserver.repository;

import com.fastcam.spserver.entity.ExercisesLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface ExercisesLogRepository extends JpaRepository<ExercisesLog, Integer> {
    List<ExercisesLog> findByMemberNum(int mnum);

    List<ExercisesLog> findByMemberNumAndIndateBetweenOrderByIndateDesc(int id, Timestamp start, Timestamp end);

    void deleteByMemberNum(int mnum);
}
