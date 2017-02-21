package me.loki2302.webdriver;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;

public class WebDriverScreenshotStrategy implements ScreenshotStrategy {
    @Autowired
    private WebDriver webDriver;

    @Override
    public File makeScreenshot() {
        return ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
    }
}
