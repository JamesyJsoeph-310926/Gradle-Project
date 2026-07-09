package com.ust.sdet.week3.pages;

import com.ust.sdet.week3.support.Config;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class HomePage extends BasePage{
    private static By SIGN_BTN = By.cssSelector("a.button.primary");
    private static By PREVIEW_BTN = By.cssSelector("a.button.secondary");
    private static By TITLE = By.cssSelector("[id='page-title']");


    public HomePage(WebDriver driver){
        super(driver);
    }

    public HomePage open(){
        driver.get(Config.baseUrl()+"/home");
        assertTrue(visible(TITLE).getText().contains("SDET Retail Automation Lab"));
        visible(TITLE);
        visible(SIGN_BTN);
        visible(PREVIEW_BTN);
        return this;
    }

    public LoginPage sigin(){
        click(SIGN_BTN);
        return new LoginPage(driver);
    }
}
