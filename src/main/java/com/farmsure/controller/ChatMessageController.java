package com.farmsure.controller;

import com.farmsure.model.Message;
import com.farmsure.model.User;
import com.farmsure.service.MessageService;
import com.farmsure.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

@Controller
public class ChatMessageController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload Message message) {
        // Save message to database
        messageService.sendMessage(message);
        // Send message to recipient's queue
        messagingTemplate.convertAndSendToUser(
                message.getRecipient().getUsername(),
                "/queue/messages",
                message);
    }
}
