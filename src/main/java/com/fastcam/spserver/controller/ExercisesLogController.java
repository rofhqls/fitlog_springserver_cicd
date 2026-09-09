package com.fastcam.spserver.controller;

import com.fastcam.spserver.entity.ExercisesLog;
import com.fastcam.spserver.service.ExercisesLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/exerciselog")
public class ExercisesLogController {
    @Autowired
    ExercisesLogService els;

    @PostMapping("/addExercisesLog")
    public HashMap<String, Object> addExerciseLog(@RequestBody ExercisesLog exercisesLog, @RequestParam("mnum") int mnum) {
        HashMap<String, Object> map = new HashMap<>();
        els.addExercisesLog(exercisesLog, mnum);
        map.put("msg", "OK");
        return map;
    }

    @GetMapping("/exercisesLogList")
    public HashMap<String, Object> exercisesLogList(@RequestParam("mnum") int mnum) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("exerciseLogList", els.getExercisesLogs(mnum));
        return map;
    }

    @DeleteMapping("/deleteExerciseLog/{num}")
    public HashMap<String, Object> deleteExercisesLog(@PathVariable("num") int num) {
        HashMap<String, Object> map = new HashMap<>();
        els.deleteExercisesLog(num);
        map.put("msg", "OK");
        return map;
    }
}
