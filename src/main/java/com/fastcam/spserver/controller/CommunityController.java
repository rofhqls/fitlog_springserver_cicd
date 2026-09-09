package com.fastcam.spserver.controller;

import com.fastcam.spserver.entity.*;
import com.fastcam.spserver.service.CommunityService;
import jakarta.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;

@RestController
@RequestMapping("/community")
public class CommunityController {
    @Autowired
    CommunityService cs;

    @PostMapping("/writePost")
    public HashMap<String, Object> writePost(@RequestBody Community community) {
        HashMap<String, Object> map = new HashMap<>();
        int mnum = community.getMember().getNum();
        int postid = cs.writePost(community, mnum);
        map.put("postid", postid);

        return map;
    }

    @PostMapping("/updatePost")
    public HashMap<String, Object> updatePost(@RequestBody Community community) {
        HashMap<String, Object> map = new HashMap<>();
        cs.updatePost(community);
        map.put("msg", "OK");
        return map;
    }

    @DeleteMapping("/deletePost/{num}")
    public HashMap<String, Object> deletePost(@PathVariable("num") int num) {
        HashMap<String, Object> map = new HashMap<>();
        cs.deletePost(num);
        return map;
    }

    @Autowired
    ServletContext sc;

    @Value("${file.upload.path}")
    private String uploadPath;

    @PostMapping("/fileupload")
    public HashMap<String, Object> fileupload(@RequestParam("image") MultipartFile file){
        HashMap<String, Object> map = new HashMap<String, Object>();

        String path = uploadPath + "/community";

        Calendar today = Calendar.getInstance();
        long dt = today.getTimeInMillis();

        String filename = file.getOriginalFilename();
        String f1 = filename.substring(0, filename.lastIndexOf("."));
        String f2 = filename.substring(filename.lastIndexOf("."));

        String newFilename = f1 + dt + f2;
        String filePath = path + "/" + newFilename;

        try {
            File dir = new File(path);

            if (!dir.exists()) {
                dir.mkdirs();
            }

            file.transferTo(new File(filePath));

            map.put("filename", newFilename);

        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }

        return map;
    }

    @PostMapping("/addLike")
    public HashMap<String, Object> addLike(@RequestBody Likes likes) {
        HashMap<String, Object> map = new HashMap<>();
        cs.addLike(likes);
        map.put("msg", "OK");
        return map;
    }

    @GetMapping("/getLikeList")
    public HashMap<String, Object> getLikeList(@RequestParam("num") int num) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("likeList", cs.getLikeList(num));
        return map;
    }

    @PostMapping("/writeReply")
    public HashMap<String, Object> writeReply(@RequestBody Reply reply) {
        HashMap<String, Object> map = new HashMap<>();
        cs.writeReply(reply);
        return map;
    }

    @DeleteMapping("/deleteReply/{num}")
    public HashMap<String, Object> deleteReply(@PathVariable("num") int num) {
        HashMap<String, Object> map = new HashMap<>();
        cs.deleteReply(num);
        return map;
    }

    @GetMapping("/getReplyList")
    public HashMap<String, Object> getReplyList(@RequestParam("num") int num) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("replyList", cs.getReplyList(num));
        return map;
    }

    @GetMapping("/userPost")
    public HashMap<String, Object> userPost(
            @RequestParam("mnum") int mnum,
            @RequestParam(value = "page", defaultValue = "1") int page) {
        return cs.getUserPost(mnum, page);
    }


    @GetMapping("/getPostList")
    public HashMap<String, Object> getPostList(
            @RequestParam(value = "page", defaultValue = "1") int page) {
        return cs.getPostList(page);
    }

    @GetMapping("/followingPost")
    public HashMap<String, Object> followingsPost(
            @RequestParam("mnum") int mnum,
            @RequestParam(value = "page", defaultValue = "1") int page) {
        return cs.getFollowingsPost(mnum, page);
    }

    @PostMapping("/report")
    public HashMap<String, Object> reportPost(@RequestBody Report report) {
        HashMap<String, Object> map = new HashMap<>();
        cs.reportPost(report);
        map.put("msg", "OK");
        return map;
    }

    @GetMapping("/reportList")
    public HashMap<String, Object> reportList(@RequestParam("status") String status) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("reportList", cs.getReportList(status));

        return map;
    }

    @PostMapping("/processReport")
    public HashMap<String, Object> processReport(
            @RequestParam("num") int num,
            @RequestParam("memo") String memo) {
        HashMap<String, Object> map = new HashMap<>();
        cs.processReport(num, memo);
        map.put("msg", "OK");
        return map;
    }


}