package com.ust.sdet.week3.data;

import io.qameta.allure.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Epic("Framework Hardening")
@Feature("Reporting Insights")
@Owner("SDET Trainee")
class AllureReporting {

    @Test
    @Story("Categories")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Categories split flaky,test and product")
    void categoriesPutGenericFlakyRules() throws IOException{
        String categories = Files.readString(Path.of("src/test/resources/allure/categories.json"));

        int flakyIndex = categories.indexOf("\"Flaky tests\"");
        int testDefectIndex = categories.indexOf("\"Test defects (broken)\"");
        int productDefectIndex = categories.indexOf("\"Product defects\"");

        assertTrue(flakyIndex >=0);
        assertTrue(testDefectIndex > flakyIndex);
        assertTrue(productDefectIndex > flakyIndex);
        assertTrue(categories.contains("\"flaky\": true"));
        assertTrue(categories.contains("timeout|stale element|connection reset"));
    }

    @Test
    @Story("Categories")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Should appear under Product defects")
    void productDefect() {
        assertEquals(10, 20, "Incorrect cart total");
    }

    @Test
    @Story("Categories")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Should appear under Test defects (broken)")
    void brokenDefect() {
        String text = null;
        text.length();
    }

    @Test
    @Disabled
    @Story("Categories")
    @Severity(SeverityLevel.CRITICAL)
    @Description("The Test will skip and add to skipped category")
    void skippedTest() {

    }


    @Test
    @Story("Environment metadata")
    @Severity(SeverityLevel.NORMAL)
    @Description("Environment metadata answers which browser, base URL and build produced the report.")
    void environmentTemplateCarriesRunContext() throws IOException {
        List<String> lines =
                Files.readAllLines(Path.of("src/test/resources/allure/environment.properties"));
        assertTrue(lines.stream().anyMatch(line -> line.startsWith("Browser=")));
        assertTrue(lines.stream().anyMatch(line -> line.startsWith("BaseURL=")));
        assertTrue(lines.stream().anyMatch(line -> line.startsWith("Build=")));
        assertTrue(lines.stream().anyMatch(line -> line.startsWith("OS=")));
    }
    @Test
    @Story("Executive overview")
    @Severity(SeverityLevel.CRITICAL)
    @Description("The leadership view is the Overview page once categories, history, severity and environment exist.")
    void executiveViewNeedsFourSignals() {
        List<String> signals = List.of(
                "status",
                "trend",
                "category split",
                "environment"
        );
        assertEquals(4, signals.size());
        assertTrue(signals.contains("trend"));
        assertTrue(signals.contains("category split"));
    }
    @Test
    @Story("Executive overview")
    @Severity(SeverityLevel.CRITICAL)
    @Description("The leadership view is the Overview page once categories, history, severity and environment exist.")
    void forfailingtest() {
        List<String> signals = List.of(
                "status",
                "trend",
                "category split",
                "environment"
        );
        assertEquals(4, signals.size());
        assertTrue(signals.contains("nottrend"));
        assertTrue(signals.contains("category split"));
    }
    void forfailingtest2() {
        List<String> signals = List.of("invlidstatus", "trend", "category split", "environment");
        assertEquals(4, signals.size());
        assertTrue(signals.contains("not trend"));
        assertTrue(signals.contains(0));
    }

}