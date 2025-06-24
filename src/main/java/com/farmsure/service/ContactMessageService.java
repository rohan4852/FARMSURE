package com.farmsure.service;

import com.farmsure.model.ContactMessage;
import com.farmsure.repository.ContactMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContactMessageService {
    @Autowired
    private ContactMessageRepository contactMessageRepository;

    public ContactMessage save(ContactMessage message) {
        return contactMessageRepository.save(message);
    }
}
