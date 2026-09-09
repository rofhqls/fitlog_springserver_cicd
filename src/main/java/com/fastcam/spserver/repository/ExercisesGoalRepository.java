package com.fastcam.spserver.repository;

import com.fastcam.spserver.entity.ExercisesGoal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExercisesGoalRepository extends JpaRepository<ExercisesGoal, Integer> {
    ExercisesGoal findByMemberNum(int num);

    void deleteByMemberNum(int mnum);
}
