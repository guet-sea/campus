package com.sea.controller;

import com.sea.bean.Chat;
import com.sea.dao.ChatMapper;
import com.sea.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class ChatController {
    @Autowired
    private ChatMapper chatMapper;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @MessageMapping("/chat")
    public void chat(Principal principal, Chat chat) {
        String from = principal.getName();
        chat.setFrom(from);

        //存库
        String msgTime = DateUtil.getyyyyMMddHHmmss();
        chat.setMsgTime(msgTime);
        chatMapper.insert(chat);

        messagingTemplate.convertAndSendToUser(chat.getTo(),
                "/queue/chat", chat);
    }
}
