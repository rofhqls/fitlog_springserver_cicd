package com.fastcam.spserver.controller;

import com.fastcam.spserver.dto.NutritionDto;
import com.fastcam.spserver.dto.RequestDto;
import com.fastcam.spserver.dto.ResponseDto;
import com.fastcam.spserver.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/ai")
@CrossOrigin({"http://3.39.239.23:8000", "http://54.180.95.196:3000", "http://54.180.95.196"})
public class AIController {

    @Autowired
    AIService as;

    @PostMapping("/query")
    public ResponseEntity<ResponseDto> query(@RequestBody RequestDto req) {
        ResponseDto res = as.query(req);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/findFood")
    public Map<String, Object> findFood(@RequestParam("image") MultipartFile file, @RequestParam("mnum") int mnum) {
        return as.findFood(file, mnum);
    }

}