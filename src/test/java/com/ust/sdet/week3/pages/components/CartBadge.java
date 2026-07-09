package com.ust.sdet.week3.pages.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CartBadge {
    private static final  By COUNT = By.cssSelector("[data-test='cart-count']");

    private final WebDriverWait wait;

    public CartBadge(WebDriverWait wait){
        this.wait = wait;
    }

//    public int count(){
//        return Integer.parseInt(wait.until(ExpectedConditions.visibilityOfElementLocated(by)).getText());
//    }

    public void expectedCount(int expected) {
        String actual = wait.until(driver -> driver.findElement(COUNT).getText());
        assertEquals(String.valueOf(expected),actual);
    }
}
