package me.loki2302.webdriver;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;

public class WebDriverUtils {
    private final static Logger LOGGER = LoggerFactory.getLogger(WebDriverUtils.class);

    @Autowired
    private WebDriver webDriver;

    public File makeScreenshot() {
        return ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
    }
}
