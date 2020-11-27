package com.dreamteam.emotianali.service;

import com.dreamteam.emotianali.entity.Tone;
import com.dreamteam.emotianali.entity.User;
import lombok.RequiredArgsConstructor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalystService {
    private final UserService userService;

    public List<Tone> getUserInfo(String username) {
        User requiredUser = (User) userService.loadUserByUsername(username);
        return requiredUser.getTones();
    }

    public List<Tone> getAllInfo() {
        List<User> users = userService.getAllUsers();
        Tone anger = new Tone("Anger", (float) 0);
        Tone fear = new Tone("Fear", (float) 0);
        Tone joy = new Tone("Joy", (float) 0);
        Tone sadness = new Tone("Sadness", (float) 0);
        Tone analytical = new Tone("Analytical", (float) 0);
        Tone confident = new Tone("Confident", (float) 0);
        for (User user : users) {
            for (Tone userTone : user.getTones()) {
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
        }
        return Arrays.asList(anger, fear, joy, sadness, analytical, confident);
    }

    public byte[] returnBarChartImage(List<Tone> tones) {
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Tone tone : tones) {
            dataset.addValue(tone.getScore(), tone.getToneName(), "Emotions of users");
        }

        JFreeChart barChart = ChartFactory.createBarChart(
                "Emotions of users",
                "Emotions",
                "Score",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false
        );
        try {
            File barChartImage = File.createTempFile( "barChart", ".jpeg");
            ChartUtils.saveChartAsJPEG(barChartImage, barChart, 480, 640);
            return Files.readAllBytes(barChartImage.toPath());
        } catch (IOException ignored) {
            return null;
        }
    }

    public byte[] returnPieChartImage(List<Tone> tones) {
        final DefaultPieDataset dataset = new DefaultPieDataset();
        for (Tone tone : tones) {
            dataset.setValue( tone.getToneName(), tone.getScore());
        }
        JFreeChart pieChart = ChartFactory.createPieChart(
                "Emotions of users",
                dataset,
                true, true, false
        );
        try {
            File pieChartImage = File.createTempFile("pieChart", ".jpeg");
            ChartUtils.saveChartAsJPEG(pieChartImage, pieChart, 480, 640);
            return Files.readAllBytes(pieChartImage.toPath());
        } catch (IOException ignored) {
            return null;
        }
    }
}
