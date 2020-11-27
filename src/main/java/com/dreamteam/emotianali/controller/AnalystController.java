package com.dreamteam.emotianali.controller;

import com.dreamteam.emotianali.entity.Tone;
import com.dreamteam.emotianali.service.AnalystService;
import com.dreamteam.emotianali.service.UserService;
import lombok.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AnalystController {
    private final AnalystService analystService;
    private final UserService userService;

    @GetMapping("/analyst")
    public String userList(Model model) {
        model.addAttribute("allUsers", userService.getAllUsers());
        return "analyst";
    }

    @PostMapping("/analyst")
    public String getInfo(@RequestParam(defaultValue = "") String username,
                          @RequestParam(defaultValue = "") String action,
                          Model model) {
        if (action.equals("get_info")) {
            List<Tone> userTones = analystService.getUserInfo(username);
            model.addAttribute("tones", userTones);
            model.addAttribute("requiredUsername", username);
        } else if (action.equals("get_full_info")) {
            List<Tone> allTones = analystService.getAllInfo();
            model.addAttribute("allTones", allTones);
            model.addAttribute("header", "Statistics for all users");
        }
        model.addAttribute("allUsers", userService.getAllUsers());
        return "analyst";
    }

    @PostMapping("/analyst/statistics")
    public String getStatistics(@RequestParam String format, Model model) {
        List<Tone> allTones = analystService.getAllInfo();

        if (format.equals("bar")) {
            byte[] barChartImage = analystService.getBarChartImage(allTones);
            String encoded = new String(Base64.getEncoder().encode(barChartImage), StandardCharsets.UTF_8);
            model.addAttribute("image", encoded);
        } else if (format.equals("pie")) {
            byte[] pieChartImage = analystService.returnPieChartImage(allTones);
            String encoded = new String(Base64.getEncoder().encode(pieChartImage), StandardCharsets.UTF_8);
            model.addAttribute("image", encoded);
        }
        model.addAttribute("allTones", allTones);
        model.addAttribute("header", "Statistics for all users");
        model.addAttribute("allUsers", userService.getAllUsers());
        return "analyst";
    }

}
