package com.fastcam.spserver.repository;

import com.fastcam.spserver.entity.Reply;
import com.fastcam.spserver.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReplyRepository extends JpaRepository<Reply, Integer> {
    Optional<Reply> findByNum(int num);

    List<Reply> findByCommunityNumOrderByIndateDesc(int num);

    void deleteByMemberNum(int mnum);

    void deleteByCommunityNum(int num);
}
