package me.loki2302.app;

import me.loki2302.app.services.NoteService;
import me.loki2302.webdriver.ScreenshotWriter;
import me.loki2302.webdriver.WebDriverConfiguration;
import me.loki2302.webdriver.WebDriverUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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
        screenshotWriter.write("main.png", webDriverUtils.makeScreenshot());
    }

    // TODO: consider using Javadoc to describe the scenario. Example:
    /**
     * === Creating a note
     *
     * Making notes is crucial. Let's get started with an empty {app}, where there are no notes at all.
     * See how empty it is. It is up to us to create the very first note.
     *
     * image::{snippetsDir}/ScreenshotTest/documentCreateNoteScenario/1.png[]
     *
     * To do so, we first type note text into a text box:
     *
     * image::{snippetsDir}/ScreenshotTest/documentCreateNoteScenario/2.png[]
     *
     * After it, we click the "Create" button. And once we do that, a new note should appear on the
     * list of note:
     *
     * image::{snippetsDir}/ScreenshotTest/documentCreateNoteScenario/3.png[]
     *
     * Done.
     */
    @Test
    public void documentCreateNoteScenario() {
        webDriver.get("http://localhost:8080/");
        screenshotWriter.write("1.png", webDriverUtils.makeScreenshot());

        WebElement textInputElement = webDriver.findElement(By.cssSelector("input[type=\"text\"]"));
        textInputElement.sendKeys("My first note");
        screenshotWriter.write("2.png", webDriverUtils.makeScreenshot());

        WebElement submitButtonElement = webDriver.findElement(By.cssSelector("button[type=\"submit\"]"));
        submitButtonElement.click();
        screenshotWriter.write("3.png", webDriverUtils.makeScreenshot());
    }

    @Configuration
    @Import(WebDriverConfiguration.class)
    public static class Config {
    }
}
