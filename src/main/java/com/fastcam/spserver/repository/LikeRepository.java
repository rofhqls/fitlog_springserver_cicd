package com.fastcam.spserver.repository;

import com.fastcam.spserver.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface LikeRepository extends JpaRepository<Likes, Integer> {
    Likes findByMemberNumAndCommunityNum(int mnum, int cnum);

    ArrayList<Likes> findByCommunityNum(int cnum);

    void deleteByCommunityNum(int num);

    void deleteByMemberNum(int mnum);
}
