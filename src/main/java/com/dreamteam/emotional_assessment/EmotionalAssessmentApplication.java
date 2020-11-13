package com.dreamteam.emotional_assessment;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EmotionalAssessmentApplication {

    public static void main(String[] args) {
        final SpringApplication app = new SpringApplication(EmotionalAssessmentApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }
}
