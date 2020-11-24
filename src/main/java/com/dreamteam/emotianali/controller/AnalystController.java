package com.dreamteam.emotianali.controller;

import com.dreamteam.emotianali.entity.Tone;
import com.dreamteam.emotianali.service.AnalystService;
import com.dreamteam.emotianali.service.UserService;
import lombok.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AnalystController {
    private final AnalystService analystService;
    private final UserService userService;

    @GetMapping("/analyst")
    public String userList(Model model) {
        model.addAttribute("allUsers", userService.allUsers());
        return "analyst";
    }

    @PostMapping("/analyst") // todo - not delete - change it
    public String  getInfoAboutUser(@RequestParam(defaultValue = "") String username,
                                 @RequestParam(defaultValue = "") String action,
                                 Model model) {
        if (action.equals("get_info")) {
            List<Tone> userTones = analystService.getUserInfo(username);
            model.addAttribute("tones", userTones);
            model.addAttribute("requiredUsername", username);
        }
        model.addAttribute("allUsers", userService.allUsers());
        return "analyst";
    }

    @GetMapping("/analyst/get/{userId}")
    public String  gtUser(@PathVariable("userId") Long userId, Model model) {
        model.addAttribute("allUsers", userService.usergtList(userId));
        return "analyst";
    }
}
