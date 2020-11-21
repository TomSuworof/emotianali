package com.dreamteam.emotianali.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.*;

@Service
public class InstagramService {
    private final String INSTAGRAM_ID = System.getenv("INSTAGRAM_ID");
    private final String INSTAGRAM_CLIENT_SECRET = System.getenv("INSTAGRAM_CLIENT_SECRET");
    private final String INSTAGRAM_REDIRECT_URI = System.getenv("INSTAGRAM_REDIRECT_URI");

    public String getCode() {
        return "https://api.instagram.com/oauth/authorize?" +
                "client_id=" + INSTAGRAM_ID +
                "&redirect_uri=" + INSTAGRAM_REDIRECT_URI +
                "&scope=user_profile,user_media" +
                "&response_type=code";
    }

    public String getToken(String code) {
        String url = "https://api.instagram.com/oauth/access_token";
        String answer = "";
        try {
            answer = Request.Post(url)
                    .addHeader("X-Custom-header", "Emotianali")
                    .bodyForm(Form.form()
                            .add("client_id", INSTAGRAM_ID)
                            .add("client_secret", INSTAGRAM_CLIENT_SECRET)
                            .add("grant_type", "authorization_code")
                            .add("redirect_uri", INSTAGRAM_REDIRECT_URI)
                            .add("code", code)
                            .build())
                    .execute().returnContent().asString();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        JsonObject jsonObject = new Gson().fromJson(answer, JsonObject.class);

        if (!jsonObject.get("error").getAsString().isEmpty()) {
            throw new RuntimeException("Invalid token");
        }
        return jsonObject.get("access_token").getAsString();
    }

    public List<String> getPosts(String token) {
        System.out.println(token);
        return new ArrayList<>();
    }
}
