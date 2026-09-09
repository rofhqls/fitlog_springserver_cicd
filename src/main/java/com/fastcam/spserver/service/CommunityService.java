package com.fastcam.spserver.service;

import com.fastcam.spserver.dto.Paging;
import com.fastcam.spserver.entity.*;
import com.fastcam.spserver.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CommunityService {
    @Autowired
    CommunityRepository cr;

    @Autowired
    MemberRepository mr;

    public int writePost(Community community, int mnum) {
        Member member = mr.findByNum(mnum);
        community.setMember(member);
        Community insertCommunity = cr.save(community);
        return insertCommunity.getNum();
    }

    public void updatePost(Community community) {
        Optional<Community> result = cr.findByNum(community.getNum());

        if (result.isPresent()) {
            Community oldCommunity = result.get();

            oldCommunity.setTitle(community.getTitle());
            oldCommunity.setContent(community.getContent());
        }
    }

    public void deletePost(int num) {
        Optional<Community> result = cr.findByNum(num);
        if(result.isPresent()) {
            Community community = result.get();
            rr.deleteByCommunityNum(num);
            repr.deleteByCommunityNum(num);
            lr.deleteByCommunityNum(num);
            cr.delete(community);
        }
    }

    @Autowired
    LikeRepository lr;

    public void addLike(Likes likes) {
        Likes findLike = lr.findByMemberNumAndCommunityNum(
                likes.getMember().getNum(),
                likes.getCommunity().getNum());
        if(findLike == null) lr.save(likes);
        else lr.delete(findLike);
    }


    public ArrayList<Likes> getLikeList(int num) {
        return lr.findByCommunityNum(num);
    }

    @Autowired
    ReplyRepository rr;

    public void writeReply(Reply reply) {
        Member member = mr.findByNum(reply.getMember().getNum());
        reply.setMember(member);
        rr.save(reply);
    }

    public void deleteReply(int num) {
        Optional<Reply> result = rr.findByNum(num);
        if(result.isPresent()) {
            Reply reply = result.get();
            rr.delete(reply);
        }
    }

    public List<Reply> getReplyList(int num) {
        return rr.findByCommunityNumOrderByIndateDesc(num);
    }

    public HashMap<String, Object> getUserPost(int mnum, int page) {
        HashMap<String, Object> map = new HashMap<>();

        Paging paging = new Paging();
        paging.setPage(page);

        int count = cr.countByMemberNumAndStatus(mnum, "Y");

        paging.setTotalCount(count);
        paging.calPaging();

        List<Community> list = cr.getUserPost(
                mnum,
                "Y",
                paging.getStartNum(),
                paging.getDisplayRow()
        );

        map.put("postList", list);
        map.put("paging", paging);

        return map;
    }

    public HashMap<String, Object> getPostList(int page) {
        HashMap<String, Object> map = new HashMap<>();

        Paging paging = new Paging();
        paging.setPage(page);

        int count = cr.countByStatus("Y");

        paging.setTotalCount(count);
        paging.calPaging();

        List<Community> list = cr.getPostList(
                "Y",
                paging.getStartNum(),
                paging.getDisplayRow()
        );

        map.put("postList", list);
        map.put("paging", paging);

        return map;
    }

    @Autowired
    FollowRepository fr;

    public HashMap<String, Object> getFollowingsPost(int mnum, int page) {
        HashMap<String, Object> map = new HashMap<>();

        List<Follow> followList = fr.findByFfrom(mnum);

        List<Integer> mnums = new ArrayList<>();

        for (Follow f : followList) {
            mnums.add(f.getFto());
        }

        Paging paging = new Paging();
        paging.setPage(page);

        if (mnums.isEmpty()) {
            paging.setTotalCount(0);
            paging.calPaging();

            map.put("postList", new ArrayList<>());
            map.put("paging", paging);

            return map;
        }

        // 팔로우한 사람들의 게시글 전체 개수
        List<Community> allList =
                cr.findByMemberNumInAndStatusOrderByIndateDesc(mnums, "Y");

        int count = allList.size();

        paging.setTotalCount(count);
        paging.calPaging();

        // 현재 페이지 게시글
        List<Community> list = cr.getFollowingsPost(
                mnums,
                "Y",
                paging.getStartNum(),
                paging.getDisplayRow()
        );

        map.put("postList", list);
        map.put("paging", paging);

        return map;
    }

    @Autowired
    ReportRepository repr;

    public void reportPost(Report report) {
        repr.save(report);
    }

    public List<Report> getReportList(String reportStatus) {
        if(reportStatus.equals("wait")) {
            return repr.findByMemoIsNull();
        } else {
            return repr.findByMemoIsNotNull();
        }
    }

    public void processReport(int num, String memo) {
        Optional<Report> result = repr.findById(num);
        if(result.isPresent()) {
            Report report = result.get();
            report.setMemo(memo);
            Community community = report.getCommunity();
            community.setStatus("N");
        }
    }
}