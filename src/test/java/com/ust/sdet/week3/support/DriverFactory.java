package com.ust.sdet.week3.support;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.HashMap;
import java.util.Map;

public final class DriverFactory {
    private DriverFactory() {
    }
    public static WebDriver createdChromeDriver() {
        ChromeOptions options = new ChromeOptions();
        Map<String, Object> prefs = new HashMap<>();

        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("profile.password_manager_leak_detection", false);

        options.setExperimentalOption("prefs", prefs);
        if(Config.headless()) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--window-size=1440,900");
        return new ChromeDriver(options);
    }

}
