package com.fastcam.spserver.repository;

import com.fastcam.spserver.entity.Community;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CommunityRepository extends JpaRepository<Community, Integer> {

    List<Community> findByMemberNum(int mnum);

    void deleteByMemberNum(int mnum);

    Optional<Community> findByNum(int num);

    List<Community> findByStatusOrderByIndateDesc(String y);

    List<Community> findByMemberNumAndStatusOrderByIndateDesc(int num, String status);

    List<Community> findByMemberNumInAndStatusOrderByIndateDesc(List<Integer> mnums, String y);

    int countByMemberNumAndStatus(int mnum, String y);

    int countByStatus(String y);

    @Query(value = """
        SELECT *
        FROM community
        WHERE mnum = :mnum
        AND status = :status
        ORDER BY indate DESC
        LIMIT :displayRow OFFSET :startNum
        """, nativeQuery = true)
    List<Community> getUserPost(
            int mnum,
            String status,
            int startNum,
            int displayRow
    );

    @Query(value = """
        SELECT *
        FROM community
        WHERE status = :status
        ORDER BY indate DESC
        LIMIT :displayRow OFFSET :startNum
        """, nativeQuery = true)
    List<Community> getPostList(
            String status,
            int startNum,
            int displayRow
    );

    @Query(value = """
        SELECT *
        FROM community
        WHERE mnum IN (:mnums)
        AND status = :status
        ORDER BY indate DESC
        LIMIT :displayRow OFFSET :startNum
        """, nativeQuery = true)
    List<Community> getFollowingsPost(
            List<Integer> mnums,
            String status,
            int startNum,
            int displayRow
    );
}