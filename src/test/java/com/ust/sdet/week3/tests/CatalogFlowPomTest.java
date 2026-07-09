package com.ust.sdet.week3.tests;

import com.ust.sdet.week3.pages.*;
import com.ust.sdet.week3.support.DriverFactory;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CatalogFlowPomTest {
    private static WebDriver driver;

    @BeforeAll
    static void setup(){
        driver = DriverFactory.createdChromeDriver();
    }

    @AfterAll
    static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void searchFindings(){
        CatalogPage catalog = new CatalogPage(driver).open().searchFor("headphones","Showing 1 product");

        List<String> titles = catalog.titles();

        assertAll(
                ()-> assertFalse(titles.isEmpty(),"Search product not found"),
                ()-> assertTrue(titles.stream().allMatch((title)->title.toLowerCase().contains("headphones")),"search result should be related to headphones")
        );
    }

    @Test
    @DisplayName("POM sort query based on the price")
    void sortProductsBasedOnthePrice(){
        CatalogPage catalogPage = new CatalogPage(driver)
                .open()
                .sortBy("Price: Low to High");

        List<Integer> prices = catalogPage.prices();
        assertEquals(prices.stream().sorted().toList(),prices);

    }

    @Test
    @DisplayName("POM full journey")
    void full_journey_of_all_stages(){

        LoginPage loginPage = new HomePage(driver)
                .open()
                .sigin();

        assertTrue(
                loginPage.getTitle()
                        .contains("Sign in to Retail Lab")
        );

        CatalogPage catalogPage = new CatalogPage(driver)
                .open()
                .searchFor("headphones","Showing 1 product");

        ProductPage productPage = catalogPage.openFirstProduct();
        assertTrue(productPage.name().toLowerCase().contains("headphones"));

        CartPage cartPage = productPage.addToCart();
        cartPage.header().cartBadge().expectedCount(1);

        assertAll(
                ()->assertEquals(1,cartPage.lineCount()),
                ()->assertFalse(cartPage.total().isBlank())
        );

        String Confirmation = cartPage.proceed()
                .placeOrder().confirmationText();

        assertTrue(Confirmation.toLowerCase().contains("confirmed"));
    }
}
