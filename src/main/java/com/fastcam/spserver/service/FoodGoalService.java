package com.fastcam.spserver.service;

import com.fastcam.spserver.entity.FoodGoal;
import com.fastcam.spserver.repository.FoodGoalRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class FoodGoalService {

    @Autowired
    FoodGoalRepository fgr;

    public void goalSave(FoodGoal foodGoal) {
        int mnum = foodGoal.getMember().getNum();
        FoodGoal existGoal = fgr.findByMemberNum(mnum);

        if (existGoal != null) {
            existGoal.setGoalCalories(foodGoal.getGoalCalories());
            existGoal.setGoalCarbs(foodGoal.getGoalCarbs());
            existGoal.setGoalProtein(foodGoal.getGoalProtein());
            existGoal.setGoalFat(foodGoal.getGoalFat());

            fgr.save(existGoal);
        } else {
            fgr.save(foodGoal);
        }
    }

    public FoodGoal getFoodGoal(int num) {
        return fgr.findByMemberNum(num);
    }
}
