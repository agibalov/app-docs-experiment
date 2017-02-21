package me.loki2302.webdriver;

import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;

public class WebDriverUtils {
    @Autowired
    private ScreenshotStrategy screenshotStrategy;

    public File makeScreenshot() {
        return screenshotStrategy.makeScreenshot();
    }
}
