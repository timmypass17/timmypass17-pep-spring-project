package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private AccountRepository accountRespository;

    public Message createMessage(Message message) {
        if (message.getMessageText().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Message text is blank");
        }

        if (message.getMessageText().length() > 255) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Message text is over 255 characters");
        }

        if (!accountRespository.existsById(message.getPostedBy())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Message poster does not exist");
        }

        return messageRepository.save(message);
    }

    public List<Message> getMessages() {
        return messageRepository.findAll();
    }

    public Optional<Message> getMessageById(Integer messageId) {
        return messageRepository.findById(messageId);
    }

    public Boolean deleteMessageById(Integer messageId) {
        boolean messageExists = messageRepository.existsById(messageId);
        if (messageExists) {
            messageRepository.deleteById(messageId);    // throws if error (cannot be handled in catch block for some reason)
        }
        return messageExists;
    } 

    public Message updateMessageById(Integer messageId, String messageText) {

        if (messageText.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Message text is blank");
        }

        if (messageText.length() > 255) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Message text is over 255 characters");
        }

        if (!messageRepository.existsById(messageId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Message poster does not exist");
        }

        Message existingMessage = messageRepository.getById(messageId);
        existingMessage.setMessageText(messageText);
        return messageRepository.save(existingMessage);
    } 

    public List<Message> getMessagesByAccountId(Integer accountId) {
        return messageRepository.findAllByPostedBy(accountId);
    }
}