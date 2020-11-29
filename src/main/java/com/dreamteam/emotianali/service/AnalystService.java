package com.dreamteam.emotianali.service;

import com.dreamteam.emotianali.entity.Tone;
import com.dreamteam.emotianali.entity.User;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.*;
import org.jfree.chart.*;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AnalystService {
    private final UserService userService;

    public List<Tone> getUserInfo(String username) {
        User requiredUser = (User) userService.loadUserByUsername(username);
        return requiredUser.getTones();
    }

    public List<Tone> getFullInfo() {
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

    public byte[] getBarChartImage(List<Tone> tones) {
        try {
            File barChartImage = File.createTempFile( "barChart", ".jpeg");
            ChartUtils.saveChartAsJPEG(barChartImage, getBarChart(tones), 480, 640);
            return Files.readAllBytes(barChartImage.toPath());
        } catch (IOException ignored) {
            return null;
        }
    }

    private JFreeChart getBarChart(List<Tone> tones) {
        return ChartFactory.createBarChart(
                "Emotions of users",
                "Emotions",
                "Score",
                getBarDataset(tones),
                PlotOrientation.VERTICAL,
                true, true, false
        );
    }

    private DefaultCategoryDataset getBarDataset(List<Tone> tones) {
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Tone tone : tones) {
            dataset.addValue(tone.getScore(), tone.getToneName(), "Emotions of users");
        }
        return dataset;
    }

    public byte[] returnPieChartImage(List<Tone> tones) {
        try {
            File pieChartImage = File.createTempFile("pieChart", ".jpeg");
            ChartUtils.saveChartAsJPEG(pieChartImage, getPieChart(tones), 480, 640);
            return Files.readAllBytes(pieChartImage.toPath());
        } catch (IOException ignored) {
            return null;
        }
    }

    private JFreeChart getPieChart(List<Tone> tones) {
        return ChartFactory.createPieChart(
                "Emotions of users",
                getPieDataset(tones),
                true, true, false
        );
    }

    private DefaultPieDataset getPieDataset(List<Tone> tones) {
        final DefaultPieDataset dataset = new DefaultPieDataset();
        for (Tone tone : tones) {
            dataset.setValue( tone.getToneName(), tone.getScore());
        }
        return dataset;
    }

    private Map<User, List<Tone>> getUsersAndTones(List<User> users) {
        Map<User, List<Tone>> usersAndTones = new HashMap<>();
        for (User user : users) {
            List<Tone> tones = user.getTones();
            tones.sort(Comparator.comparing(Tone::getToneName));
            usersAndTones.put(user, tones);
        }
        return usersAndTones;
    }

    public File getXLSXFile(List<User> users) {
        Map<User, List<Tone>> usersAndTones = getUsersAndTones(users);
        try {
            File xlsxFile = File.createTempFile("statEmotions", ".xlsx");
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("Emotions");
            HSSFRow heading = sheet.createRow(0);

            List<Tone> tones = (List<Tone>) usersAndTones.values().toArray()[0];

            heading.createCell(0).setCellValue("Username");
            for (int i = 0; i < tones.size(); i++) {
                heading.createCell(i + 1).setCellValue(tones.get(i).getToneName());
            }
            ArrayList<Map.Entry<User, List<Tone>>> notSet = new ArrayList<>(usersAndTones.entrySet());
            for (Map.Entry<User, List<Tone>> pair : notSet) {
                HSSFRow row = sheet.createRow(notSet.indexOf(pair) + 1);
                row.createCell(0).setCellValue(pair.getKey().getUsername());
                for (int i = 0; i < pair.getValue().size(); i++) {
                    row.createCell(i + 1).setCellValue(pair.getValue().get(i).getScore());
                }
            }
            workbook.write(xlsxFile);
            workbook.close();
            return xlsxFile;
        } catch (IOException ignored) {
            return null;
        }
    }

    public File getCSVFile(List<User> users) {
        Map<User, List<Tone>> usersAndTones = getUsersAndTones(users);
        try {
            File csvFile = File.createTempFile("statEmotions", ".csv");
            FileWriter writer = new FileWriter(csvFile);
            writer.append("Username").append(",");

            List<Tone> tones = (List<Tone>) usersAndTones.values().toArray()[0];

            for (Tone tone : tones) {
                writer.append(tone.getToneName());
                if (tones.indexOf(tone) != tones.size() - 1) {
                    writer.append(",");
                }
            }
            writer.append("\n");

            for (Map.Entry<User, List<Tone>> pair : usersAndTones.entrySet()) {
                writer.append(pair.getKey().getUsername()).append(",");
                for (Tone tone : pair.getValue()) {
                    writer.append(String.valueOf(tone.getScore()));
                    if (pair.getValue().indexOf(tone) != pair.getValue().size() - 1) {
                        writer.append(",");
                    }
                }
                writer.append("\n");
            }

            writer.close();
            return csvFile;
        } catch (IOException ignored) {
            return null;
        }
    }
}
