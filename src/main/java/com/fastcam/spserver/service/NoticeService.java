package com.fastcam.spserver.service;

import com.fastcam.spserver.entity.Notice;
import com.fastcam.spserver.repository.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class NoticeService {
    @Autowired
    NoticeRepository nr;

    public int writeNotice(Notice notice) {
        Notice insertNotice = nr.save(notice);
        return insertNotice.getNum();
    }

    public void updateNotice(Notice notice) {
        Optional<Notice> result = nr.findByNum(notice.getNum());

        if(result.isPresent()) {
            Notice oldNotice = result.get();
            oldNotice.setTitle(notice.getTitle());
            oldNotice.setContent(notice.getContent());
        }
    }

    public void deleteNotice(int num) {
        Optional<Notice> result = nr.findByNum(num);
        if(result.isPresent()) {
            Notice notice = result.get();
            nr.delete(notice);
        }
    }

    public List<Notice> getAllList() {
        return nr.findAll();
    }

    public Notice getNotice(int num) {
        return nr.findByNum(num).orElse(null);
    }
}
