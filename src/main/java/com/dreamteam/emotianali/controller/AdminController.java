package com.dreamteam.emotianali.controller;

import com.dreamteam.emotianali.entity.Tone;
import com.dreamteam.emotianali.service.AnalystService;
import com.dreamteam.emotianali.service.UserService;
import lombok.*;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;
    private final AnalystService analystService;
    private File fileStat;

    @GetMapping("/admin")
    public String userList(Model model) {
        model.addAttribute("allUsers", userService.getAllUsers());
        return "admin";
    }

    @PostMapping("/admin")
    public String changeUser(@RequestParam(defaultValue = "") Long userId,
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
        } else if (action.equals("get_full_info")) {
            List<Tone> allTones = analystService.getFullInfo();
            model.addAttribute("allTones", allTones);
            model.addAttribute("header", "Statistics for all users");
        }
        model.addAttribute("allUsers", userService.getAllUsers());
        return "admin";
    }

    @PostMapping("/admin/statistics")
    public String getStatistics(@RequestParam String format,
                                Model model) {
        if (format.equals("excel")) {
            fileStat = analystService.getExcelFile(userService.getAllUsers());
            model.addAttribute("filename", fileStat.getName());
        }
        List<Tone> allTones = analystService.getFullInfo();
        model.addAttribute("allTones", allTones);
        model.addAttribute("header", "Statistics for all users");
        model.addAttribute("allUsers", userService.getAllUsers());
        return "admin";
    }

    @RequestMapping(value = "/admin/statistics/file", method = RequestMethod.GET, produces = "application/xlsx")
    public @ResponseBody
    Resource getFile(HttpServletResponse response) {
        response.setContentType("application/xlsx");
        response.setHeader("Content-Disposition", "inline; filename=" + fileStat.getName());
        response.setHeader("Content-Length", String.valueOf(fileStat.length()));
        return new FileSystemResource(fileStat);
    }
}
