package com.ust.sdet.week3.bdd;

import com.ust.sdet.week3.pages.CartPage;
import com.ust.sdet.week3.pages.CatalogPage;
import com.ust.sdet.week3.pages.CheckoutPage;
import com.ust.sdet.week3.pages.ProductPage;
import com.ust.sdet.week3.pages.components.Header;
import org.openqa.selenium.WebDriver;

public class World {
    public WebDriver driver;
    public CatalogPage catalog;
    public ProductPage product;
    public CartPage cart;
    public CheckoutPage checkout;

    public Header header() {
        return new Header(driver);
    }
}
