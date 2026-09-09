package com.fastcam.spserver.controller;


import com.fastcam.spserver.entity.FoodLog;
import com.fastcam.spserver.service.FoodLogService;
import jakarta.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;

@RestController
@RequestMapping("/foodLog")
public class FoodLogController {
    @Autowired
    FoodLogService fls;

    @Autowired
    ServletContext sc;

    @PostMapping("/fileupload")
    public HashMap<String, Object> fileupload(@RequestParam("image") MultipartFile file) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        String path = sc.getRealPath("/image/community");
        Calendar today = Calendar.getInstance();
        long dt = today.getTimeInMillis();
        String filename = file.getOriginalFilename();
        String f1 = filename.substring(0, filename.lastIndexOf("."));
        String f2 = filename.substring(filename.lastIndexOf("."));
        String uploadPath = path + "/" + f1 + dt + f2;
        try {
            file.transferTo(new File(uploadPath));
            map.put("filename", f1 + dt + f2);
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }
        return map;
    }


    @PostMapping("/addFoodLog")
    public HashMap<String, Object> addFoodLog(@RequestBody FoodLog foodLog, @RequestParam("mnum") int mnum) {
        HashMap<String, Object> map = new HashMap<>();
        fls.addFoodLog(foodLog, mnum);
        map.put("msg", "OK");
        return map;
    }

    @GetMapping("/foodLogList")
    public HashMap<String, Object> foodLogList(@RequestParam("mnum") int mnum) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("foodLogList", fls.getFoodLog(mnum));
        return map;
    }

    @DeleteMapping("/deleteFoodLog/{num}")
    public HashMap<String, Object> deleteFoodLog(@PathVariable("num") int num) {
        HashMap<String, Object> map = new HashMap<>();
        fls.deleteFoodLog(num);
        map.put("msg", "OK");
        return map;
    }
}
