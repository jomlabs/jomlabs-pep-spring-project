package com.example.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }


    public Optional<Account> registerAccount(Account account){
         
        //username not blank
        if(account.getUsername() ==null || account.getUsername().isBlank()){
            return Optional.empty();
        }


        if(account.getPassword() ==null || account.getPassword().length() <4){
            return Optional.empty();
        }

        //checking if username exist
        if(accountRepository.findByUsername(account.getUsername()).isPresent()){
            return Optional.empty();
        }

        //save new account
        Account savedAccount = accountRepository.save(account);

        return Optional.of(savedAccount);

    }



    public Optional<Account> loginAccount(Account account){
        
        if(account.getUsername() == null || account.getUsername().isBlank() ||
        account.getPassword() ==null || account.getPassword().isBlank()){
            return Optional.empty();
        }

        Optional <Account> foundAccount   = accountRepository.findByUsername(account.getUsername());

        if(foundAccount.isPresent()){
            if(foundAccount.get().getPassword().equals(account.getPassword())){
                return foundAccount;
            }
        }
        //if login fails
        return Optional.empty();
    }


    public Optional<Account> findById(int accountId){
        return accountRepository.findById(accountId);
    }


    public Optional<Account> findByUsername(String username) {
        return accountRepository.findByUsername(username);
    }
}
