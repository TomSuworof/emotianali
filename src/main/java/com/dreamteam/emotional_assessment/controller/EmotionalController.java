package com.dreamteam.emotional_assessment.controller;

import com.dreamteam.emotional_assessment.entity.Tone;
import com.dreamteam.emotional_assessment.service.TelegramService;
import com.dreamteam.emotional_assessment.service.TranslateService;
import com.dreamteam.emotional_assessment.service.ToneService;
import lombok.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequiredArgsConstructor
public class EmotionalController {
    private final TelegramService telegramService;
    private final ToneService toneService;
    private final TranslateService translateService;

//    @GetMapping("/emotional_assessment_start")
//    public String returnCode(@RequestParam String phoneNumber) {
//        // todo here should be some code for telegram api (?)
//        String authKeyId = telegramService.getAuthKeyId(phoneNumber);
//        return "emotional_assessment";
//    }

    @GetMapping("/emotional_assessment")
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