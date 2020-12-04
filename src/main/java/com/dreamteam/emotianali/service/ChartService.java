package com.dreamteam.emotianali.service;

import com.dreamteam.emotianali.entity.Tone;
import lombok.RequiredArgsConstructor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.SpiderWebPlot;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChartService {
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

    private CategoryDataset getBarDataset(List<Tone> tones) {
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Tone tone : tones) {
            dataset.addValue(tone.getScore(), tone.getToneName(), "Emotions of users");
        }
        return dataset;
    }

    public byte[] getPieChartImage(List<Tone> tones) {
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
            dataset.setValue(tone.getToneName(), tone.getScore());
        }
        return dataset;
    }

    public byte[] getRadarChartImage(List<Tone> tones) {
        try {
            File radarChartImage = File.createTempFile("radarChart", ".jpeg");
            ChartUtils.saveChartAsJPEG(radarChartImage, getRadarChart(tones), 480, 640);
            return Files.readAllBytes(radarChartImage.toPath());
        } catch (IOException ignored) {
            return null;
        }
    }

    private JFreeChart getRadarChart(List<Tone> tones) {
        return new JFreeChart("Emotions of users", new SpiderWebPlot(getRadarDataset(tones)));
    }

    private CategoryDataset getRadarDataset(List<Tone> tones) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Tone tone : tones) {
            dataset.addValue(tone.getScore(), tone.getToneName(), tone.getToneName());
        }
        return dataset;
    }
}
