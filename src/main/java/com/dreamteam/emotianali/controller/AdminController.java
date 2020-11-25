package com.dreamteam.emotianali.controller;

import com.dreamteam.emotianali.service.UserService;
import lombok.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;

    @GetMapping("/admin")
    public String userList(Model model) {
        model.addAttribute("allUsers", userService.allUsers());
        return "admin";
    }

    @PostMapping("/admin")
    public String deleteUser(@RequestParam(defaultValue = "") Long userId,
                             @RequestParam(defaultValue = "") String action,
                             Model model) {
        if (action.equals("delete")) {
            if (!userService.changeRole(userId, "blocked")) {
                model.addAttribute("error", "Failed to delete user");
            }
        } else if (action.equals("make_analyst")) {
            if (!userService.changeRole(userId, "analyst")) {
                model.addAttribute("error", "Failed to set user as Analyst");
            }
        } else if (action.equals("make_user")) {
            if (!userService.changeRole(userId, "user")) {
                model.addAttribute("error", "Failed to set user as User");
            }
        }
        model.addAttribute("allUsers", userService.allUsers());
        return "admin";
    }

    @GetMapping("/admin/get/{userId}")
    public String  gtUser(@PathVariable("userId") Long userId, Model model) {
        model.addAttribute("allUsers", userService.usergtList(userId));
        return "admin";
    }
}
