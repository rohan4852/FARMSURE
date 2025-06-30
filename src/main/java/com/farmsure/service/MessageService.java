package com.farmsure.service;

import com.farmsure.model.Message;
import com.farmsure.model.User;
import com.farmsure.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    public Message sendMessage(Message message) {
        return messageRepository.save(message);
    }

    public List<Message> getReceivedMessages(User user) {
        return messageRepository.findByRecipientOrderByCreatedAtDesc(user);
    }

    public List<Message> getSentMessages(User user) {
        return messageRepository.findBySenderOrderByCreatedAtDesc(user);
    }

    public Message getMessage(Long id) {
        return messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found"));
    }

    public void markAsRead(Message message) {
        message.setRead(true);
        messageRepository.save(message);
    }

    public long getUnreadCount(User user) {
        return messageRepository.countByRecipientAndReadFalse(user);
    }

    public Integer getUnreadCountForUser(User user) {
        return (int) getUnreadCount(user);
    }

    public void deleteMessage(Long id) {
        messageRepository.deleteById(id);
    }

    public java.util.List<Message> getConversation(User user1, User user2) {
        return messageRepository.findConversation(user1, user2);
    }
}
