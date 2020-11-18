package com.dreamteam.emotional_assessment.controller;

import com.dreamteam.emotional_assessment.service.UserService;
import lombok.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class AnalystController {
    private final UserService userService;

    @GetMapping("/analyst")
    public String userList(Model model) {
        model.addAttribute("allUsers", userService.allUsers());
        return "analyst";
    }

    @PostMapping("/analyst") // todo - not delete - change it
    public String  getInfoAboutUser(@RequestParam(defaultValue = "") Long userId,
                                 @RequestParam(defaultValue = "") String action,
                                 Model model) {
        if (action.equals("get_info")) {
//            userService.deleteUser(userId);
            System.out.println("was request for information for user" + userId); // todo
        }
        return "analyst";
    }

    @GetMapping("/analyst/get/{userId}")
    public String  gtUser(@PathVariable("userId") Long userId, Model model) {
        model.addAttribute("allUsers", userService.usergtList(userId));
        return "analyst";
    }
}
