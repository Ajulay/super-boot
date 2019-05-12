package com.ajulay.controller;

import com.ajulay.model.Message;
import com.ajulay.repository.MessageRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class GreetingController {

    @Autowired
    private MessageRepository messageRepository;

    @GetMapping("/")
    public String getHello(){
        return "greeting";
    }

    @GetMapping("/main")
    public String main(Map<String, Object> model){
        model.put("messages", messageRepository.findAll());
        return "main";
    }
    @PostMapping("/main")
    public String addMain(@RequestParam String text, @RequestParam  String tag, Map<String, Object> model){
        Message message = new Message(text, tag);
        messageRepository.save(message);
        model.put("messages", messageRepository.findAll());
        return "main";
    }

    @PostMapping("/filter")
    public String filter(@RequestParam  String tag, Map<String, Object> model){
        if(tag == null || tag.isEmpty()){
            model.put("messages", messageRepository.findAll());
        } else
            model.put("messages", messageRepository.findAllByTag(tag));
        return "main";
    }
}
