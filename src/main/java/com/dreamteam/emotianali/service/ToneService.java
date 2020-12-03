package com.dreamteam.emotianali.service;

import com.dreamteam.emotianali.entity.Record;
import com.dreamteam.emotianali.entity.Tone;
import com.dreamteam.emotianali.entity.User;
import com.google.gson.*;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.tone_analyzer.v3.model.ToneAnalysis;
import com.ibm.watson.tone_analyzer.v3.model.ToneOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ToneService {
    private final UserService userService;
    private final String APIKEY = System.getenv("TONE_APIKEY");
    private final String URL = System.getenv("TONE_URL");

    public List<Tone> getAssessment(String text) {
        List<Tone> tones = collectTones(parseJson(getJSONAssessment(text)));

        if (addUserAssessments(text, tones)) {
            return tones;
        } else {
            throw new RuntimeException();
        }
    }

    private String getJSONAssessment(String text) {
        IamAuthenticator authenticator = new IamAuthenticator(APIKEY);
        ToneAnalyzer toneAnalyzer = new ToneAnalyzer("2017-09-21", authenticator);
        toneAnalyzer.setServiceUrl(URL);

        ToneOptions toneOptions = new ToneOptions.Builder()
                .text(text)
                .build();

        ToneAnalysis toneAnalysis = toneAnalyzer.tone(toneOptions).execute().getResult();
        return toneAnalysis.toString();
    }

    private JsonArray parseJson(String assessmentJson) {
        return new Gson()
                .fromJson(assessmentJson, JsonObject.class)
                .getAsJsonObject("document_tone")
                .getAsJsonArray("tones");
    }

    private List<Tone> collectTones(JsonArray tonesJson) {
        List<Tone> tones = new ArrayList<>();
        for (JsonElement tone : tonesJson) {
            JsonObject toneObject = new Gson().fromJson(tone, JsonObject.class);
            String toneName = toneObject.getAsJsonPrimitive("tone_name").getAsString();
            float score = toneObject.getAsJsonPrimitive("score").getAsFloat();
            tones.add(new Tone(toneName, score));
        }
        return tones;
    }

    private boolean addUserAssessments(String text, List<Tone> tones) {
        User currentUser = userService.getUserFromContext();
        Set<Record> userRecords = currentUser.getRecords();
        userRecords.add(new Record(text, Set.copyOf(tones)));
        currentUser.setRecords(userRecords);
        return userService.updateUser(currentUser, false);
    }

    public List<String> getAdvices(List<Tone> tones) {
        String angerAdvice = "To cope with anger, you need to hold your breath and count to 10, then exhale slowly and to the end; clench your fists and breathe in your belly very measurably, deeply, and slowly. Relaxing your muscles also helps to cope with anger.";
        String fearAdvice = "Try to relax, breathe deeply and slowly. Motivate yourself not to be afraid of anything, try to understand what the reason for your fear was. Moreover, you may talk with people with the same fear – together you can find the ways of dealing with it. Visiting psychologist is helpful too.";
        String sadnessAdvice = "Try to realise the reasons of your sadness. Spend time with some of your close people, talk with them, do something nice and relaxing. Take a hot bath, drink a cup of tea or warm milk. Respect your feelings, spend some time with yourself – it can really help to deal with sadness.";
        String analyticalAdvice = "You are too tense, too busy. It can cause overworking symptoms. Please, try to exhale and relax. Let go all the thoughts that are stuck in your head, calm down and take a rest. This can be useful for dealing with overwork.";
        String joyAdvice = "Wow! The feeling of joy is a great feeling. We hope you slept well, ate delicious food, and had fun! Continue in the same spirit!";
        String confidentAdvice = "You're confident! Keep going. You will achieve all your goals!";
        String tentativeAdvice = "The best strategy for dealing with being tentative is to compare your present self with your past. This analysis will help you see your personal growth and progress in the area that you have been working on. Don't be afraid to step out of your comfort zone and always look for new opportunities.";

        List<String> advices = new ArrayList<>();
        for (Tone tone : tones) {
            if (tone.getToneName().equals("Anger") && tone.getScore() > 0.6) {
                advices.add(angerAdvice);
            }
            if (tone.getToneName().equals("Fear") && tone.getScore() > 0.6) {
                advices.add(fearAdvice);
            }
            if (tone.getToneName().equals("Sadness") && tone.getScore() > 0.6) {
                advices.add(sadnessAdvice);
            }
            if (tone.getToneName().equals("Analytical") && tone.getScore() > 0.6) {
                advices.add(analyticalAdvice);
            }
            if (tone.getToneName().equals("Joy") && tone.getScore() > 0.6) {
                advices.add(joyAdvice);
            }
            if (tone.getToneName().equals("Confident") && tone.getScore() > 0.6) {
                advices.add(confidentAdvice);
            }
            if (tone.getToneName().equals("Tentative") && tone.getScore() > 0.6) {
                advices.add(tentativeAdvice);
            }
        }
        return advices;
    }
}
