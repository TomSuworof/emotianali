package com.dreamteam.emotianali.service;

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
        String assessment = getJSONAssessment(text);
        JsonArray tonesJson = parseJson(assessment);
        List<Tone> tones = collectTones(tonesJson);

        if (addUserAssessments(tones)) {
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

    private boolean addUserAssessments(List<Tone> tones) {
        User currentUser = userService.getUserFromContext();
        Set<Tone> userTones = currentUser.getTones();
        userTones.addAll(tones);
        currentUser.setTones(userTones);
        return userService.updateUser(currentUser, false);
    }
}
