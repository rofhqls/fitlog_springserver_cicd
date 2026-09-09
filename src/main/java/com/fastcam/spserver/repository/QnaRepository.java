package com.fastcam.spserver.repository;


import com.fastcam.spserver.entity.Qna;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QnaRepository extends JpaRepository<Qna, Integer> {


    void deleteByNum(int num);

    Qna findByNum(int num);

    List<Qna> findByMemberNum(int mnum);

    List<Qna> findByReplyIsNull();

    List<Qna> findByReplyIsNotNull();
}
