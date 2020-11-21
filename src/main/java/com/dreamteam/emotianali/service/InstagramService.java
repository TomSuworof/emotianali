package com.dreamteam.emotianali.service;

import com.dreamteam.emotianali.entity.InstagramPost;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.springframework.stereotype.Service;

import java.io.IOException;
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
                "&scope=instagram_graph_user_media+instagram_graph_user_profile" +
                "&response_type=code";
    }

    public List<InstagramPost> getPosts(String code) {
        String answer = getTokenAndUserId(code);
        JsonObject jsonObject = new Gson().fromJson(answer, JsonObject.class);
        String accessToken = jsonObject.get("access_token").getAsString();
        String userId = jsonObject.get("user_id").getAsString();
        String userData = getUserData(accessToken);
        JsonArray jsonData = new Gson()
                .fromJson(userData, JsonObject.class)
                .get("data").getAsJsonArray();
        List<InstagramPost> instagramPosts = new ArrayList<>();
        for (JsonElement jsonPost : jsonData) {
            InstagramPost instagramPost = new InstagramPost(
                    jsonPost.getAsJsonObject().get("id").getAsLong(),
                    jsonPost.getAsJsonObject().get("caption").getAsString()
            );
            instagramPosts.add(instagramPost);
        }
        return instagramPosts;
    }

    private String getTokenAndUserId(String code) {
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

        return answer;
    }

    private String getUserData(String accessToken) {
        String url = "https://graph.instagram.com/me/media";
        String answer = "";
        try {
            answer = Request.Get(url + "?fields=id,caption&limit=100&access_token=" + accessToken)
                    .execute().returnContent().asString();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return answer;
    }
}
