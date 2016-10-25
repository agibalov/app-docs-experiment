package me.loki2302.app;

import me.loki2302.documentation.SnippetWriter;
import me.loki2302.spring.advanced.EnableTransactionLogging;
import me.loki2302.webdriver.FrontEndTransactionFacade;
import me.loki2302.webdriver.WebDriverConfiguration;
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

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
        App.class,
        AdvancedTransactionTest.Config.class
}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AdvancedTransactionTest {
    @Rule
    public SnippetWriter screenshotWriter = new SnippetWriter(System.getProperty("snippetsDir"));

    @Autowired
    private WebDriver webDriver;

    @Autowired
    private FrontEndTransactionFacade frontEndTransactionFacade;

    @Test
    public void describeLoadNotesTransactions() {
        // no reset needed, since it's empty
        // frontEndTransactionFacade.reset();

        webDriver.get("http://localhost:8080/");

        List<String> records = frontEndTransactionFacade.getRecords();
        assertEquals(15, records.size());
    }

    @Test
    public void describeCreateNoteTransaction() {
        webDriver.get("http://localhost:8080/");

        WebElement textInputElement = webDriver.findElement(By.cssSelector("input[type=\"text\"]"));
        textInputElement.sendKeys("My first note");

        WebElement submitButtonElement = webDriver.findElement(By.cssSelector("button[type=\"submit\"]"));

        frontEndTransactionFacade.reset();

        submitButtonElement.click();

        List<String> records = frontEndTransactionFacade.getRecords();
        assertEquals(36, records.size());
    }

    @Configuration
    @EnableTransactionLogging
    @Import(WebDriverConfiguration.class)
    public static class Config {
    }
}
