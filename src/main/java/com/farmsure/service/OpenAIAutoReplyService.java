package com.farmsure.service;

import org.springframework.stereotype.Service;

@Service
public class OpenAIAutoReplyService {

    public String generateReply(String userName, String userMessage) {
        // Placeholder implementation for AI reply generation
        return "Thank you " + userName + " for your message. We will get back to you shortly.";
    }
}
