package com.fastcam.spserver.repository;

import com.fastcam.spserver.entity.FoodGoal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodGoalRepository extends JpaRepository<FoodGoal, Integer> {
    FoodGoal findByNum(int num);

    FoodGoal findByMemberNum(int num);

    void deleteByMemberNum(int mnum);
}
