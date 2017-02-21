package me.loki2302.webdriver;

import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Requires java.awt.headless to be false
 */
public class AwtScreenshotStrategy implements ScreenshotStrategy {
    @Autowired
    private WebDriver webDriver;

    @Override
    public File makeScreenshot() {
        WebDriver.Window window = webDriver.manage().window();
        int width = window.getSize().getWidth();
        int height = window.getSize().getHeight();
        int left = window.getPosition().getX();
        int top = window.getPosition().getY();

        Robot robot;
        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }

        BufferedImage bufferedImage = robot.createScreenCapture(new Rectangle(
                new Point(left, top),
                new Dimension(width, height)));
        File file;
        try {
            file = Files.createTempFile("screenshot", "png").toFile();
            ImageIO.write(bufferedImage, "png", file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return file;
    }
}
