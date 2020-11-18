package com.dreamteam.emotianali;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EmotianaliApplication {

    public static void main(String[] args) {
        final SpringApplication app = new SpringApplication(EmotianaliApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }
}
