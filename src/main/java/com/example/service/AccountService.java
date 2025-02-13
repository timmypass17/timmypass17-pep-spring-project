package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Account register(Account account) {
        if (account.getUsername().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password must be at least 4 characters");
        }

        if (account.getPassword().length() < 4) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password must be at least 4 characters");
        }

        if (accountRepository.existsByUsername(account.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Account ID already exists");
        }

        return accountRepository.save(account);
    }

    public Account login(Account account) {
        Account existingAccount = accountRepository.findByUsernameAndPassword(account.getUsername(), account.getPassword())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
        return existingAccount;
    }

}
