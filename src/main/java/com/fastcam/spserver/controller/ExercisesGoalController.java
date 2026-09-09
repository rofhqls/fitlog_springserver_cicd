package com.fastcam.spserver.controller;

import com.fastcam.spserver.entity.ExercisesGoal;
import com.fastcam.spserver.service.ExercisesGoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/exercisesgoal")
public class ExercisesGoalController {
    @Autowired
    ExercisesGoalService egs;

    @PostMapping("/insertExercisesGoal")
    public HashMap<String, Object> insertExercisesGoal(@RequestBody ExercisesGoal exercisesGoal) {
        HashMap<String, Object> map = new HashMap<>();
        egs.insertExercisesGoal(exercisesGoal);
        map.put("msg", "OK");
        return map;
    }

    @GetMapping("/getExercisesGoal/{num}")
    public HashMap<String, Object> getExercisesGoal(@PathVariable("num") int num) {
        HashMap<String, Object> map = new HashMap<>();
        ExercisesGoal goal = egs.getExercisesGoal(num);
        map.put("goal", goal);
        return map;
    }
}
