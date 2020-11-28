package com.dreamteam.emotianali.service;

import com.dreamteam.emotianali.entity.Tone;
import com.dreamteam.emotianali.entity.User;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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

    public File getExcelFile(List<User> users) {
        Map<User, List<Tone>> usersAndTones = new HashMap<>();
        for (User user : users) {
            List<Tone> tones = user.getTones();
            tones.sort(Comparator.comparing(Tone::getToneName));
            usersAndTones.put(user, tones);
        }
        try {
//            File excelFile = File.createTempFile("statExcel", ".xlsx");
            File excelFile = new File("statExcel.xls");
            HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(excelFile));
            HSSFSheet sheet = workbook.createSheet("Emotions");
            HSSFRow heading = sheet.createRow(0);
//            for (int i = 0; i < users.get(0).getTones().size(); i++) {
//                heading.createCell(i).setCellValue(users.get(0).getTones().get(i).getToneName());
//            }
            List<Tone> tones = (List<Tone>) usersAndTones.values().toArray()[0];

            heading.createCell(0).setCellValue("Username");
            for (int i = 0; i < tones.size(); i++) {
                heading.createCell(i + 1).setCellValue(tones.get(i).getToneName());
            }
            ArrayList<Map.Entry<User, List<Tone>>> notSet = new ArrayList<>(usersAndTones.entrySet());
            for (Map.Entry<User, List<Tone>> pair : notSet) {
                HSSFRow row = sheet.createRow(notSet.indexOf(pair));
                row.createCell(0).setCellValue(pair.getKey().getUsername());
                for (int i = 0; i < pair.getValue().size(); i++) {
                    row.createCell(i + 1).setCellValue(pair.getValue().get(i).getScore());
                }
            }
            workbook.close();
            return excelFile;
        } catch (IOException ignored) {
            return null;
        }
    }
}
