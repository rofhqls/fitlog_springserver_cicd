package com.fastcam.spserver.controller;

import com.fastcam.spserver.dto.ChatHistoryDto;
import com.fastcam.spserver.entity.Nutrition;
import com.fastcam.spserver.service.AIService;
import com.fastcam.spserver.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    AIService as;

    @Autowired
    ChatService cs;

    @GetMapping("/goal/{mnum}")
    public HashMap<String, Object> getUserGoal(@PathVariable("mnum") int mnum){
        return as.getUserGoal(mnum);
    }

    @GetMapping("/food-logs/{mnum}")
    public HashMap<String, Object> getConsumedToday(@PathVariable("mnum") int mnum){
        return as.getConsumedToday(mnum);
    }

    @GetMapping("/foods")
    public List<Nutrition> queryFoodCandidates(@RequestParam("maxCalories") int mc){
        return as.queryFoodCandidates(mc);
    }

    @GetMapping("/getHistory/{mnum}")
    public List<ChatHistoryDto> getHistory(@PathVariable("mnum") int mnum){
        return cs.getHistory(mnum);
    }
}
