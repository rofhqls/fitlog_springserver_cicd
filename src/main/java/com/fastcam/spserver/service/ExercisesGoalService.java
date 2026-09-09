package com.fastcam.spserver.service;

import com.fastcam.spserver.entity.ExercisesGoal;
import com.fastcam.spserver.repository.ExercisesGoalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ExercisesGoalService {
    @Autowired
    ExercisesGoalRepository egr;

    public void insertExercisesGoal(ExercisesGoal exercisesGoal) {
        int mnum = exercisesGoal.getMember().getNum();
        ExercisesGoal existGoal = egr.findByMemberNum(mnum);

        if(existGoal != null) {
            existGoal.setGoalTime(exercisesGoal.getGoalTime());
            existGoal.setGoalCalories(exercisesGoal.getGoalCalories());
            existGoal.setGoalWeight(exercisesGoal.getGoalWeight());

            egr.save(existGoal);
        } else {
            egr.save(exercisesGoal);
        }
    }

    public ExercisesGoal getExercisesGoal(int num) {
        return egr.findByMemberNum(num);
    }
}
