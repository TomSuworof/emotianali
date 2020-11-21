package com.dreamteam.emotianali.controller;

import com.dreamteam.emotianali.entity.Tone;
import com.dreamteam.emotianali.service.InstagramService;
import com.dreamteam.emotianali.service.TranslateService;
import com.dreamteam.emotianali.service.ToneService;
import lombok.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequiredArgsConstructor
public class EmotionalController {
    private final InstagramService instagramService;
    private final ToneService toneService;
    private final TranslateService translateService;

    @GetMapping("/emotional_assessment/instagram")
    public String returnCode() {
        return "redirect:" + instagramService.getCode();
    }

    @GetMapping("/emotional_assessment")
    public String returnListOfPosts(@RequestParam(name = "code") String code, Model model) {
        String token = instagramService.getToken(code);
        List<String> posts = instagramService.getPosts("token");
        posts.forEach(System.out::println);
        return "emotional_assessment";
    }

    @GetMapping("/emotional_assessment/start")
    public String returnAssessment(@RequestParam(name = "text", required = false) String text, Model model) {
        if (text != null) {
            String translated = translateService.translate(text);
            List<Tone> result = toneService.getAssessment(translated);
            model.addAttribute("text", translated);
            model.addAttribute("result", result);
        }

        return "emotional_assessment";
    }
}