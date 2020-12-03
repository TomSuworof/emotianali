package com.dreamteam.emotianali.service;

import com.google.gson.*;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.cloud.sdk.core.service.exception.NotFoundException;
import com.ibm.watson.language_translator.v3.LanguageTranslator;
import com.ibm.watson.language_translator.v3.model.TranslateOptions;
import com.ibm.watson.language_translator.v3.model.TranslationResult;
import org.springframework.stereotype.Service;

@Service
public class TranslateService {
    private final String APIKEY = System.getenv("TRANSLATE_APIKEY");
    private final String URL = System.getenv("TRANSLATE_URL");

    public String translate(String text) {
        try {
            return getTranslation(translateJSON(text));
        } catch (NotFoundException languageError) {
            return text;
        }
    }

    private String translateJSON(String text) {
        IamAuthenticator authenticator = new IamAuthenticator(APIKEY);
        LanguageTranslator languageTranslator = new LanguageTranslator("2018-05-01", authenticator);
        languageTranslator.setServiceUrl(URL);

        TranslateOptions translateOptions = new TranslateOptions.Builder()
                .addText(text)
                .target("en")
                .build();

        TranslationResult result = languageTranslator.translate(translateOptions).execute().getResult();
        return result.toString();
    }

    private String getTranslation(String text) {
        return new Gson()
                .fromJson(text, JsonObject.class)
                .getAsJsonArray("translations")
                .get(0)
                .getAsJsonObject()
                .getAsJsonPrimitive("translation")
                .getAsString();
    }
}
