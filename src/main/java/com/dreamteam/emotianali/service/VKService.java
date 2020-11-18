//package com.dreamteam.emotional_assessment.service;
//
//import com.vk.api.sdk.client.TransportClient;
//import com.vk.api.sdk.client.VkApiClient;
//import com.vk.api.sdk.client.actors.UserActor;
//import com.vk.api.sdk.exceptions.ApiException;
//import com.vk.api.sdk.exceptions.ClientException;
//import com.vk.api.sdk.httpclient.HttpTransportClient;
//import com.vk.api.sdk.objects.UserAuthResponse;
//import com.vk.api.sdk.objects.messages.Message;
//import com.vk.api.sdk.objects.messages.responses.GetResponse;
//import org.springframework.stereotype.Service;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Service
//public class VKService {
//    private final Integer APP_ID = Integer.parseInt(System.getenv("APP_ID"));
//    private final String CLIENT_SECRET = System.getenv("CLIENT_SECRET");
//    private final String REDIRECT_URI = "localhost:8080/emotional_assessment";
//
//    public String getCode() {
//        return "https://oauth.vk.com/authorize?client_id=" + APP_ID + "&display=page&redirect_uri=https://oauth.vk.com/blank.html&scope=messages&response_type=token&v=5.52&state=ok";
//    }
//
//    public List<String> getUserMessages(String code) {
//        TransportClient transportClient = HttpTransportClient.getInstance();
//        try {
//            VkApiClient vk = new VkApiClient(transportClient);
//            UserAuthResponse authResponse = vk.oauth()
//                    .userAuthorizationCodeFlow(APP_ID, CLIENT_SECRET, REDIRECT_URI, code)
//                    .execute();
//
//            UserActor actor = new UserActor(authResponse.getUserId(), authResponse.getAccessToken());
//
//            GetResponse response = vk.messages().get(actor).execute();
//            List<Message> messages = response.getItems();
//            return messages.stream().map(Message::getBody).collect(Collectors.toList());
//        } catch (ApiException | ClientException exception) {
//            exception.printStackTrace();
//        }
//        return new ArrayList<>();
//    }
//}
//
//// http://localhost:8080/emotional_assessment
//
////    public String getToken(String code) throws RuntimeException {
////        final RestTemplate restTemplate = new RestTemplate();
////        String url = "https://oauth.vk.com/access_token?client_id=" + APP_ID + "&client_secret=" + CLIENT_SECRET + "&redirect_uri=http://localhost:8080/emotional_assessment&code=" + code;
////        final String answer = restTemplate.getForObject(url, String.class);
////        JsonObject jsonObject = new Gson().fromJson(answer, JsonObject.class);
////
////        if (!jsonObject.get("error").getAsString().isEmpty()) {
////            throw new RuntimeException("Invalid token");
////        }
////        return jsonObject.get("access_token").getAsString();
////    }
////
////    public List<Message> getMessages(String token) {
////        final RestTemplate restTemplate = new RestTemplate();
////        final String response = restTemplate.getForObject("https://api.vk.com/method/messages.get?out=1&access_token=" + token, String.class);
////        JsonArray jsonArray = new Gson().fromJson(response, JsonObject.class).getAsJsonObject("response").getAsJsonArray("items");
////        List<Message> userMessages = new ArrayList<>();
////        for (JsonElement jsonElement : jsonArray) {
////            JsonObject jsonObject = jsonElement.getAsJsonObject();
////            userMessages.add(new Message(
////                    jsonObject.get("id").getAsLong(),
////                    jsonObject.get("title").getAsString() + " " + jsonObject.get("body").getAsString()
////            ));
////        }
////        System.out.println(response);
////
////        return userMessages;
////    }
//
//
////    @Data
////    public static class UserFriend {
////        String firstName;
////        String lastName;
////        int id;
////        String full;
////
////        public UserFriend(String firstName, String lastName, int id) {
////            this.firstName = firstName;
////            this.lastName = lastName;
////            this.id = id;
////            this.full = this.id + " " + this.firstName + " " + this.lastName;
////        }
////    }
//
//
////        TransportClient transportClient = HttpTransportClient.getInstance();
////        VkApiClient vk = new VkApiClient(transportClient);
////
////
//////        UserAuthResponse authResponse = vk.oAuth()
//////                .userAuthorizationCodeFlow(APP_ID, CLIENT_SECRET, REDIRECT_URI, code)
//////                .execute();
//////        UserActor actor = new UserActor(authResponse.getUserId(), authResponse.getAccessToken());