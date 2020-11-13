package com.dreamteam.emotional_assessment.service;

import org.springframework.stereotype.Service;

@Service
public class TelegramService {
    private final int API_ID = Integer.parseInt(System.getenv("TELEGRAM_API_ID"));
    private final String API_HASH = System.getenv("TELEGRAM_API_HASH");

    public String getAuthKeyId(String phoneNumber) {
        String methodSent = "https://api.telegram.org/auth.sentCode?phone_number="
                + phoneNumber +
                "api_id=" + API_ID +
                "api_hash=" + API_HASH;
        System.out.println(methodSent);
        return "";
    }
}
