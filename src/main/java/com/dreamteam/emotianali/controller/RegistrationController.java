package com.dreamteam.emotianali.controller;

import com.dreamteam.emotianali.entity.User;
import com.dreamteam.emotianali.service.UserService;
import lombok.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class RegistrationController {

    private final UserService userService;

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("userForm", new User());
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@ModelAttribute("userForm") User userForm, String agreement, BindingResult bindingResult, Model model) {
        if (agreement == null) {
            model.addAttribute("agreementError", "In order to use service you should agree with terms of use");
            return "registration";
        }
        if (bindingResult.hasErrors()) {
            return "registration";
        }
        if (!userForm.getPassword().equals(userForm.getPasswordConfirm())){
            model.addAttribute("passwordError", "Passwords do not match");
            return "registration";
        }
        if (!userService.saveUser(userForm)){
            model.addAttribute("usernameError", "Username unavailable");
            return "registration";
        }

        return "redirect:/";
    }
}
