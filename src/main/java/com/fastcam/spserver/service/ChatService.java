package com.fastcam.spserver.service;

import com.fastcam.spserver.dto.ChatHistoryDto;
import com.fastcam.spserver.entity.Chat;
import com.fastcam.spserver.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ChatService {

    @Autowired
    ChatRepository cr;

    public List<ChatHistoryDto> getHistory(int mnum) {
        List<ChatHistoryDto> res = new ArrayList<>();
        List<Chat> chatList = cr.findByMemberNumOrderByNum(mnum);
        for(Chat c : chatList){
            ChatHistoryDto cdto = new ChatHistoryDto();
            cdto.setText(c.getContent());
            cdto.setType(c.getSender());
            res.add(cdto);
        }
        return res;
    }
}
