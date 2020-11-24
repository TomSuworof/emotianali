package com.dreamteam.emotianali.service;

import com.dreamteam.emotianali.entity.Tone;
import com.dreamteam.emotianali.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalystService {
    private final UserService userService;

    public List<Tone> getUserInfo(String username) {
        User requiredUser = (User) userService.loadUserByUsername(username);
        Tone anger = new Tone("Anger", (float) 0);
        Tone fear = new Tone("Fear", (float) 0);
        Tone joy = new Tone("Joy", (float) 0);
        Tone sadness = new Tone("Sadness", (float) 0);
        Tone analytical = new Tone("Analytical", (float) 0);
        Tone confident = new Tone("Confident", (float) 0);
        for (Tone userTone : requiredUser.getTones()) {
            if (userTone.getToneName().equals("Anger")) {
                anger.addScore(userTone.getScore());
            }
            if (userTone.getToneName().equals("Fear")) {
                fear.addScore(userTone.getScore());
            }
            if (userTone.getToneName().equals("Joy")) {
                joy.addScore(userTone.getScore());
            }
            if (userTone.getToneName().equals("Sadness")) {
                sadness.addScore(userTone.getScore());
            }
            if (userTone.getToneName().equals("Analytical")) {
                analytical.addScore(userTone.getScore());
            }
            if (userTone.getToneName().equals("Confident")) {
                confident.addScore(userTone.getScore());
            }
        }
        return Arrays.asList(anger, fear, joy, sadness, analytical, confident);
    }
}
