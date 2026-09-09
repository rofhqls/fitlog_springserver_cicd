package com.fastcam.spserver.service;

import com.fastcam.spserver.dto.Paging;
import com.fastcam.spserver.entity.Qna;
import com.fastcam.spserver.repository.QnaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Service
@Transactional
public class QnaService {

    @Autowired
    QnaRepository qr;

    @Transactional(readOnly = true)
    public HashMap<String, Object> getQnaList(int mnum) {
        HashMap<String, Object> result = new HashMap<>();

        List<Qna> list = qr.findByMemberNum(mnum);
        result.put("qnaList", list);
        return result;

    }

    public Qna getQna(int num) {
        return qr.findByNum(num);
    }

    public void writeQna(Qna qna) {
        qr.save(qna);
    }

    public void deleteBoard(int num) {
        qr.deleteByNum(num);
    }

    public void replyQna(int num, String reply) {
        Qna qna = qr.findById(num).get();
        qna.setReply(reply);
        qr.save(qna);
    }

    public List<Qna> getAllList(String status) {
        if ("wait".equals(status)) return qr.findByReplyIsNull();

        if ("done".equals(status)) return qr.findByReplyIsNotNull();

        return qr.findAll();
    }
}
