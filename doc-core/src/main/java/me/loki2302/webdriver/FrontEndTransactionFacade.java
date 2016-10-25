package me.loki2302.webdriver;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class FrontEndTransactionFacade {
    private final static Logger LOGGER = LoggerFactory.getLogger(FrontEndTransactionFacade.class);

    @Autowired
    private WebDriver webDriver;

    public void reset() {
        ((JavascriptExecutor)webDriver).executeScript("window.transactionRecorder.reset()");
    }

    public List<String> getRecords() {
        List<String> records = (List<String>)((JavascriptExecutor)webDriver).executeScript("return window.transactionRecorder.getRecords()");
        LOGGER.info("Retrieved {}", records);
        return records;
    }
}
