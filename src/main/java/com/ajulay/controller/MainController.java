package com.ajulay.controller;

import com.ajulay.model.Message;
import com.ajulay.model.User;
import com.ajulay.repository.MessageRepository;

import javassist.bytecode.analysis.MultiType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Controller
public class MainController {

    @Autowired
    private MessageRepository messageRepository;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/")
    public String getHello() {
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
            @Valid Message message,
            BindingResult bindingResult,
            Model model,
            @RequestParam MultipartFile file) throws IOException {
        message.setAuthor(user);
        if (bindingResult.hasErrors()) {
            model.mergeAttributes(ControllerUtil.getErrors(bindingResult));
            model.addAttribute("message", message);
        } else {
            fillFile(message, file);
            model.addAttribute("message", null);
            messageRepository.save(message);
        }
        model.addAttribute("messages", messageRepository.findAll());

        return "main";
    }

    private void fillFile(@Valid Message message, @RequestParam MultipartFile file) throws IOException {
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            String uuidfile = UUID.randomUUID().toString() + "." + file.getOriginalFilename();
            file.transferTo(new File(uploadPath + "/" + uuidfile));
            message.setFilename(uuidfile);
        }
    }

    @GetMapping("/user-messages/{user}")
    public String userMessages(@AuthenticationPrincipal User currentUser,
                               @PathVariable User user,
                               Model model,
                               @RequestParam(required = false) Message message){
        Set<Message> userMessages = user.getMessages();


        model.addAttribute("subscriptionsCount", user.getSubscriptions().size());
        model.addAttribute("subscribersCount", user.getSubscribers().size());
        model.addAttribute("isSubscriber", user.getSubscribers().contains(currentUser));
        model.addAttribute("userChannel", user);
        model.addAttribute("messages", userMessages);
        model.addAttribute("message", message);
        model.addAttribute("isCurrentUser", user.equals(currentUser));

    return "userMessages";
    }
    @PostMapping("/user-messages/{user}")
    public String updateMessages(@AuthenticationPrincipal User currentUser,
                               @PathVariable Long user,
                                 @RequestParam("id")Message message,
                                 @RequestParam("text")String text,
                                 @RequestParam("tag")String tag,
                                 @RequestParam MultipartFile file
                                 ) throws IOException {
        if (message.getAuthor().equals(currentUser)){
            if(StringUtils.isEmpty(text)){
                message.setText(text);
            }
            if(StringUtils.isEmpty(tag)){
                message.setTag(tag);
            }
            fillFile(message, file);

            messageRepository.save(message);
        }

        return "redirect:/user-messages/" + user;
    }

}
