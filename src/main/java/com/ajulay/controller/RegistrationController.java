package com.ajulay.controller;

import com.ajulay.dto.CaptchaResponseDto;
import com.ajulay.model.User;
import com.ajulay.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationController {

    private static final String CAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserService userService;

    @Value("${recaptcha.secret}")
    private String secret;

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(
            @RequestParam("password2") String passwordConfirm,
            @RequestParam("g-recaptcha-response") String response,
            @Valid User user, BindingResult bindingResult, Model model) {
        final String url = String.format(CAPTCHA_URL, secret, response);
        CaptchaResponseDto captchaResponse = restTemplate.postForObject(url, Collections.EMPTY_LIST, CaptchaResponseDto.class);
        if(!captchaResponse.isSuccess()){
            model.addAttribute("recaptchaError", "fill captcha");
        }

        boolean emptyConfirm = StringUtils.isEmpty(passwordConfirm);
        if (emptyConfirm) {
            model.addAttribute("password2Error", "Password confirmation cannot be empty");
        }
        if(user.getPassword() != null && !user.getPassword().equals(passwordConfirm)){
            model.addAttribute("passwordError", "Passwords are not equal");
        }
        if(emptyConfirm || bindingResult.hasErrors() || !captchaResponse.isSuccess()){
            Map<String, String> mapErrors = ControllerUtil.getErrors(bindingResult);
            model.mergeAttributes(mapErrors);
            return "registration";
        }

        if (!userService.addUser(user)) {
            model.addAttribute("usernameError", "user exists...");
            return "registration";
        }

        return "redirect:/login";
    }

    @GetMapping("/activate/{code}")
    public String activate(@PathVariable String code, Model model) {

        boolean isActivated = userService.activateUser(code);

        if (isActivated) {
            model.addAttribute("messageType", "success");
            model.addAttribute("message", "User successfully activated!");
        } else{
            model.addAttribute("messageType", "danger");
            model.addAttribute("message", "Activation code is not found.");
        }

        return "login";
    }

}
