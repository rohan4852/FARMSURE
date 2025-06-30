package com.farmsure.repository;

import com.farmsure.model.Message;
import com.farmsure.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByRecipientOrderByCreatedAtDesc(User recipient);

    List<Message> findBySenderOrderByCreatedAtDesc(User sender);

    long countByRecipientAndReadFalse(User recipient);

    @org.springframework.data.jpa.repository.Query("SELECT m FROM Message m WHERE (m.sender = :user1 AND m.recipient = :user2) OR (m.sender = :user2 AND m.recipient = :user1) ORDER BY m.createdAt ASC")
    java.util.List<Message> findConversation(@org.springframework.data.repository.query.Param("user1") User user1,
            @org.springframework.data.repository.query.Param("user2") User user2);
}
