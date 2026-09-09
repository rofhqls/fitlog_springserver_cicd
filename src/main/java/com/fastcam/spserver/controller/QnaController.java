package com.fastcam.spserver.controller;

import com.fastcam.spserver.entity.Qna;
import com.fastcam.spserver.service.QnaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/qna")
public class QnaController {

    @Autowired
    QnaService qs;

    @GetMapping("/getList")
    public HashMap<String, Object> qnaList(@RequestParam("mnum") int mnum) {
        HashMap<String, Object> map = qs.getQnaList(mnum);
        return map;
    }

    @GetMapping("/getQna/{num}")
    public HashMap<String, Object> getQna(@PathVariable("num") int num) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        Qna qdto = qs.getQna(num);
        map.put("qna", qdto);
        return map;
    }

    @PostMapping("/writeQna")
    public HashMap<String, Object> writeQna(@RequestBody Qna qna) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        qs.writeQna(qna);
        map.put("msg", "OK");
        return map;
    }

    @DeleteMapping("/deleteQna")
    public HashMap<String, Object> deleteQna(@RequestParam("num") int num) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        qs.deleteBoard(num);
        map.put("msg", "OK");
        return map;
    }

    @PostMapping("/replyQna")
    public HashMap<String, Object> answerQna(
            @RequestParam("num") int num,
            @RequestParam("reply") String reply) {
        HashMap<String, Object> map = new HashMap<>();
        qs.replyQna(num, reply);
        map.put("msg", "OK");
        return map;
    }

    @GetMapping("/getAllList")
    public HashMap<String, Object> getAllList(@RequestParam("status") String status) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("qnaList", qs.getAllList(status));
        return map;
    }
}
