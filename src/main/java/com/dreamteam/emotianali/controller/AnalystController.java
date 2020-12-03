package com.dreamteam.emotianali.controller;

import com.dreamteam.emotianali.entity.Tone;
import com.dreamteam.emotianali.service.AnalystService;
import com.dreamteam.emotianali.service.UserService;
import lombok.*;
import org.springframework.core.io.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class AnalystController {
    private final AnalystService analystService;
    private final UserService userService;
    private File fileStat;

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
            List<Tone> allTones = analystService.getFullInfo();
            model.addAttribute("allTones", allTones);
            model.addAttribute("header", "Statistics for all users");
        }
        model.addAttribute("allUsers", userService.getAllUsers());
        return "analyst";
    }

    @PostMapping("/analyst/statistics")
    public String getStatistics(@RequestParam String format, Model model) {
        List<Tone> allTones = analystService.getFullInfo();

        switch (format) {
            case "bar": {
                byte[] barChartImage = analystService.getBarChartImage(allTones);
                String encoded = new String(Base64.getEncoder().encode(barChartImage), StandardCharsets.UTF_8);
                model.addAttribute("image", encoded);
                break;
            }
            case "pie": {
                byte[] pieChartImage = analystService.returnPieChartImage(allTones);
                String encoded = new String(Base64.getEncoder().encode(pieChartImage), StandardCharsets.UTF_8);
                model.addAttribute("image", encoded);
                break;
            }
            case "radar": {
                byte[] radarChartImage = analystService.returnRadarChartImage(allTones);
                String encoded = new String(Base64.getEncoder().encode(radarChartImage), StandardCharsets.UTF_8);
                model.addAttribute("image", encoded);
                break;
            }
            case "excel":
                fileStat = analystService.getXLSXFile(userService.getAllUsers());
                model.addAttribute("filenameXLSX", fileStat.getName());
                break;
            case "csv":
                fileStat = analystService.getCSVFile(userService.getAllUsers());
                model.addAttribute("filenameCSV", fileStat.getName());
                break;
        }
        model.addAttribute("allTones", allTones);
        model.addAttribute("header", "Statistics for all users");
        model.addAttribute("allUsers", userService.getAllUsers());
        return "analyst";
    }

    @RequestMapping(value = "/analyst/statistics/xlsx", method = RequestMethod.GET, produces = "application/xlsx")
    public @ResponseBody Resource getFileXLSX(HttpServletResponse response) {
        response.setContentType("application/xlsx");
        response.setHeader("Content-Disposition", "inline; filename=" + fileStat.getName());
        response.setHeader("Content-Length", String.valueOf(fileStat.length()));
        return new FileSystemResource(fileStat);
    }

    @RequestMapping(value = "/analyst/statistics/csv", method = RequestMethod.GET, produces = "application/csv")
    public @ResponseBody Resource getFileCSV(HttpServletResponse response) {
        response.setContentType("application/csv");
        response.setHeader("Content-Disposition", "inline; filename=" + fileStat.getName());
        response.setHeader("Content-Length", String.valueOf(fileStat.length()));
        return new FileSystemResource(fileStat);
    }
}
