package com.fastcam.spserver.controller;

import com.fastcam.spserver.entity.FoodGoal;
import com.fastcam.spserver.entity.Qna;
import com.fastcam.spserver.service.FoodGoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/foodgoal")
public class FoodGoalController {

    @Autowired
    FoodGoalService fgs;

    @PostMapping("/goalSave")
    public HashMap<String, Object> goalSave(@RequestBody FoodGoal foodGoal) {
        HashMap<String, Object> map = new HashMap<>();
        fgs.goalSave(foodGoal);
        map.put("msg", "OK");
        return map;
    }

    @GetMapping("/getFoodGoal/{num}")
    public HashMap<String, Object> getFoodGoal(@PathVariable("num") int num) {
        HashMap<String, Object> map = new HashMap<>();
        FoodGoal fg = fgs.getFoodGoal(num);
        map.put("foodGoal", fg);
        return map;
    }

}
