package com.dreamteam.emotional_assessment.controller;

import com.dreamteam.emotional_assessment.entity.Role;
import com.dreamteam.emotional_assessment.entity.User;
import com.dreamteam.emotional_assessment.service.UserService;
import lombok.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final UserService userService;

    @GetMapping("/")
    public String returnStartPage(Model model) {
        User currentUser = userService.getUserFromContext();
        if (currentUser != null) {
            if (currentUser.getRoles().contains(new Role((long) 1, "ROLE_ADMIN"))) {
                model.addAttribute("show_admin_page", true);
            } else if (currentUser.getRoles().contains(new Role((long) 2, "ROLE_ANALYST"))) {
                model.addAttribute("show_analyst_page", true);
            }
            model.addAttribute("show_personal_area", true);
            model.addAttribute("show_logout", true);
        }
        return "index";
    }

    @GetMapping("/login")
    public String returnLogin() {
        return "login";
    }
}

// todo пробежаться и удалить комменты
