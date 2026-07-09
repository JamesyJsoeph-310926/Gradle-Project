package com.ust.sdet.week3.bdd;

import com.ust.sdet.week3.support.DriverFactory;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

public class Hooks {
    private final World world;

    public Hooks(World world){
        this.world = world;
    }

    @Before
    public void setup(){
        world.driver = DriverFactory.createdChromeDriver();
    }

    @After(order = 1)
    public void attachScreenshotOnFailure(Scenario scenario){
        if(scenario.isFailed() && world.driver instanceof TakesScreenshot screenshotDriver){
            byte[] screenshot = screenshotDriver.getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", "failure-screenshot");
        }
    }

    @After(order = 0)
    public void tearDown() {
        if(world.driver != null){
            world.driver.quit();
        }
    }
}
