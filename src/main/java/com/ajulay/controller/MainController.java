package com.ajulay.controller;

import com.ajulay.model.Message;
import com.ajulay.model.User;
import com.ajulay.repository.MessageRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class MainController {

    @Autowired
    private MessageRepository messageRepository;

    @GetMapping("/")
    public String getHello(){
        return "greeting";
    }

    @GetMapping("/main")
    public String main(@RequestParam(required = false, defaultValue = "") String tag, Model model) {
        Iterable<Message> messages;

        if (tag == null || tag.isEmpty()) {
            messages = messageRepository.findAll();

        } else {
            messages = messageRepository.findByTag(tag);

        }
        model.addAttribute("messages", messages);
        model.addAttribute("tag", tag);

        return "main";
    }
    @PostMapping("/main")
    public String addMain(
            @AuthenticationPrincipal User user,
            @RequestParam String text, @RequestParam String tag, Map<String, Object> model) {
        Message message = new Message(text, tag, user);
        messageRepository.save(message);
        model.put("messages", messageRepository.findAll());
        return "main";
    }

//    @PostMapping("/filter")
//    public String filter(@RequestParam  String tag, Map<String, Object> model){
//        if(tag == null || tag.isEmpty()){
//            model.put("messages", messageRepository.findAll());
//        } else
//            model.put("messages", messageRepository.findByTag(tag));
//        return "main";
//    }
}
