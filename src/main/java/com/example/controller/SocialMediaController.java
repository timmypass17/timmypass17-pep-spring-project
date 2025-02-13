package com.example.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

// Combines @Controller, @ResponseBody, easier controller impl, methods no loner need to include @ResponseBody before the return type
// @ResponseBody - automatically serialize return objects into HTTP responses
@RestController
public class SocialMediaController {

    private final AccountService accountService;
    private final MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    @PostMapping("register")
    public ResponseEntity<Account> register(@RequestBody Account account) {
        Account createdAccount = accountService.register(account);
        return ResponseEntity.status(HttpStatus.OK).body(createdAccount);
    }

    @PostMapping("login")
    public ResponseEntity<Account> login(@RequestBody Account account) {
        Account existingAccount = accountService.login(account);
        return ResponseEntity.status(HttpStatus.OK).body(existingAccount);
    }

    @PostMapping("messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        Message createdMessage = messageService.createMessage(message);
        return ResponseEntity.status(HttpStatus.OK).body(createdMessage);
    }

    @GetMapping("messages")
    public ResponseEntity<List<Message>> getMessages() {
        List<Message> messages = messageService.getMessages();
        return ResponseEntity.status(HttpStatus.OK).body(messages);
    }

    @GetMapping("messages/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable Integer messageId) {
        Optional<Message> message = messageService.getMessageById(messageId);
    
        if (message.isPresent()) {
            return ResponseEntity.ok(message.get());
        } else {
            return ResponseEntity.ok().build();
        }
    }
    
    @DeleteMapping("messages/{messageId}")
    public ResponseEntity<Integer> deleteMessageById(@PathVariable Integer messageId) {
        if (messageService.deleteMessageById(messageId)) {
            return ResponseEntity.ok(1);
        } else {
            return ResponseEntity.ok().build();
        }
    }

    @PatchMapping("messages/{messageId}")
    public ResponseEntity<Integer> updateMessageById(@PathVariable Integer messageId, @RequestBody Message message) {
        messageService.updateMessageById(messageId, message.getMessageText());
        return ResponseEntity.status(HttpStatus.OK).body(1);
    }

    @GetMapping("accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getMessagesByAccountId(@PathVariable Integer accountId) {
        List<Message> messages = messageService.getMessagesByAccountId(accountId);
        return ResponseEntity.ok(messages);
    }

    @ExceptionHandler(ResponseStatusException.class)    // catch this exception from service
    public ResponseEntity<String> handleResponseStatusException(ResponseStatusException ex) {
        return ResponseEntity.status(ex.getStatus()).body(ex.getReason());
    }

    // @ExceptionHandler(MissingServletRequestParameterException.class)
    // public ResponseEntity<String> handleMissingParams(MissingServletRequestParameterException ex) {
    //     return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getParameterName() + " is missing in the query parameters and is required.");
    // }

    // @ExceptionHandler(Exception.class)
    // public ResponseEntity<String> handleGeneralException(Exception ex) {
    //     return ResponseEntity.internalServerError().body("An unexpected error occurred.");
    // }
}
