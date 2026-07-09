package com.ust.sdet.week3.pages;

import com.ust.sdet.week3.support.Config;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginPage extends BasePage{
    private static By EMAIL = By.cssSelector("[id='email']");
    private static By PASSWORD = By.cssSelector("[id='password']");
    private static By TITLE = By.cssSelector("#login-title");
    private static By SUBMIT_BTN = By.cssSelector(".button.primary.form-submit");

    public LoginPage(WebDriver driver){
        super(driver);
    }

    public LoginPage open(){
        driver.get(Config.loginUrl());
        assertTrue(visible(TITLE).getText().contains("Sign in to Retail Lab"));
        visible(EMAIL);
        visible(PASSWORD);
        visible(SUBMIT_BTN);
        return this;
    }

    public LoginPage login(String email, String password){
        type(EMAIL,email);
        type(PASSWORD, password);
        click(SUBMIT_BTN);
        return this;
    }

    public String getTitle()
    {
      return  text(TITLE);

    }


}
