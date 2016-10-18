package me.loki2302.app;

import me.loki2302.app.services.NoteService;
import me.loki2302.webdriver.ScreenshotWriter;
import me.loki2302.webdriver.WebDriverConfiguration;
import me.loki2302.webdriver.WebDriverUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
        App.class,
        ScreenshotTest.Config.class
}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ScreenshotTest {
    @Rule
    public ScreenshotWriter screenshotWriter = new ScreenshotWriter(System.getProperty("snippetsDir"));

    @Autowired
    private WebDriver webDriver;

    @Autowired
    private WebDriverUtils webDriverUtils;

    @Autowired
    private NoteService noteService;

    @Test
    public void documentScreenshot() {
        noteService.createNote("I am test note one");
        noteService.createNote("I am test note two");
        noteService.createNote("I am test note three");
        noteService.createNote("I am test note four");
        noteService.createNote("I am test note five");

        webDriver.get("http://localhost:8080/");
        webDriverUtils.synchronizeAngular2();
        screenshotWriter.write("main.png", webDriverUtils.makeScreenshot());
    }

    @Configuration
    @Import(WebDriverConfiguration.class)
    public static class Config {
    }
}
