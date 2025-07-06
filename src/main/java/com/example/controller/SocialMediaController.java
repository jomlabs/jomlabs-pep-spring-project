package com.example.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RestController
@RequestMapping("/")
public class SocialMediaController {

    private final AccountService accountService;
    private final MessageService messageService;



    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService){
        this.accountService =accountService;
        this.messageService = messageService;
    }

    @PostMapping("/register")
    public ResponseEntity<Account> registerAccount(@RequestBody Account account){

        if(account.getUsername() != null && !account.getUsername().isBlank()  && accountService.findByUsername(account.getUsername()).isPresent()){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Optional<Account>  registeredAccount = accountService.registerAccount(account);

        if(registeredAccount.isPresent()) {
            return ResponseEntity.ok(registeredAccount.get());
        }else{
            return ResponseEntity.badRequest().build();
        }

    }


    @PostMapping("/login")
    public ResponseEntity<Account> loginAccount(@RequestBody  Account account){
        Optional<Account> loggedInAccount = accountService.loginAccount(account);

        if(loggedInAccount.isPresent()){
            return ResponseEntity.ok(loggedInAccount.get());
        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }



    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message){
        Optional<Message>  createdMessage = messageService.createMessage(message);

        if(createdMessage.isPresent()){
            return ResponseEntity.ok(createdMessage.get());
        }else{
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages(){
        List<Message> messages = messageService.getAllMessages();
        return ResponseEntity.ok(messages);
    }

    @GetMapping("messages/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable int messageId){
        Optional<Message> message = messageService.getMessageById(messageId);

        if(message.isPresent()){
            return ResponseEntity.ok(message.get());
        }else{
            return ResponseEntity.ok().build(); //empty body
        }
    }

    @DeleteMapping("messages/{messageId}")
    public ResponseEntity<Integer> deleteMessageById(@PathVariable int messageId){
        int deleteCount = messageService.deleteMessage(messageId);

        if(deleteCount >0){
            return ResponseEntity.ok(deleteCount);
        }else{
            return ResponseEntity.ok().build();
        }
    }

    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<Integer> updateMessageText(@PathVariable int messageId, @RequestBody Message messageUpdate){
        String newMessageText  = messageUpdate.getMessageText();

        int updateCount = messageService.updateMessageText(messageId, newMessageText);
        if(updateCount >0){
            return ResponseEntity.ok(updateCount); //200 ok with body
        }else{
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getMessagesByAccountId(@PathVariable int accountId){
        List<Message> messages = messageService.getMessagesByAccountId(accountId);
        return ResponseEntity.ok(messages);

    }


}
