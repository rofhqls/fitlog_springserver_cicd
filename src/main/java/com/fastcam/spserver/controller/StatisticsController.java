package com.fastcam.spserver.controller;

import com.fastcam.spserver.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/statistics")
public class StatisticsController {
    @Autowired
    StatisticsService ss;

    @GetMapping("/summary")
    public HashMap<String, Object> getSummary(
            @RequestParam("id") int id,
            @RequestParam("period") String period,
            @RequestParam("date") String date
    ) {
        return ss.getSummary(id, period, date);
    }

}
