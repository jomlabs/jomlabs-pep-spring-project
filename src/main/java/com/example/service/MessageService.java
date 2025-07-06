package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final AccountService accountService;


    @Autowired
    public MessageService(MessageRepository messageRepository, AccountService accountService){
        this.messageRepository = messageRepository;
        this.accountService = accountService;
    }


    //new message

    public Optional<Message> createMessage(Message message){

        //not blank
        if(message.getMessageText() ==null || message.getMessageText().isBlank() || message.getMessageText().length() >255){
            return Optional.empty();
        }


        //posted by real, existing user

        if(message.getPostedBy() ==null || accountService.findById(message.getPostedBy()).isEmpty()){
            return Optional.empty();
        }

        if(message.getTimePostedEpoch()==null|| message.getTimePostedEpoch() == 0L){ //checking if it's not set
            message.setTimePostedEpoch(System.currentTimeMillis());
        }


        //save the new message
        Message savedMessage = messageRepository.save(message);
        return Optional.of(savedMessage); 
    }



    public List<Message> getAllMessages(){
        return messageRepository.findAll();
    }


    public Optional<Message> getMessageById(int messageId){
        return messageRepository.findById(messageId);

    }


    public int deleteMessage(int messageId){
        if(messageRepository.existsById(messageId)){
            messageRepository.deleteById(messageId);
            return 1;
        }

        return 0;
    }

    public int updateMessageText(int messageId, String newMessageText){
        if(newMessageText == null || newMessageText.isBlank() || newMessageText.length() >255 ){
            return 0;
        }
        Optional<Message> existingMessageOptional  = messageRepository.findById(messageId);

        if(existingMessageOptional.isPresent()){
            Message existMessage = existingMessageOptional.get();

            existMessage.setMessageText(newMessageText);
            messageRepository.save(existMessage);
            return 1;
        }

        return 0;// no message found
    }


    public List<Message> getMessagesByAccountId(int accountId){
        return messageRepository.findByPostedBy(accountId);
    }


}
