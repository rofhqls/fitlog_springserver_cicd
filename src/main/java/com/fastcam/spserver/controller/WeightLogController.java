package com.fastcam.spserver.controller;

import com.fastcam.spserver.entity.WeightLog;
import com.fastcam.spserver.service.WeightLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/weightlog")
public class WeightLogController {

    @Autowired
    WeightLogService wls;

    @GetMapping("/getWeightLog/{num}")
    public HashMap<String, Object> getWeightLog(@PathVariable("num") int num) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        List<WeightLog> weightLog = wls.getWeightLog(num);
        map.put("weightGoal", wls.getWeightGoal(num));
        map.put("weightLog", weightLog);
        return map;
    }

    @PostMapping("/writeWeightLog")
    public HashMap<String, Object> writeWeightLog(@RequestBody WeightLog weightLog){
        HashMap<String, Object> map = new HashMap<String, Object>();
        wls.writeWeightLog(weightLog);
        map.put("msg", "OK");
        return map;
    }


}
