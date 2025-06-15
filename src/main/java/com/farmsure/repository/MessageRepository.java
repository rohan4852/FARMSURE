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
}
