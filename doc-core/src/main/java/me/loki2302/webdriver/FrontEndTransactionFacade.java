package me.loki2302.webdriver;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.loki2302.spring.TransactionEvent;
import me.loki2302.spring.TransactionEventCollectionWrapper;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;

public class FrontEndTransactionFacade {
    private final static Logger LOGGER = LoggerFactory.getLogger(FrontEndTransactionFacade.class);

    @Autowired
    private WebDriver webDriver;

    @Autowired
    private ObjectMapper objectMapper;

    public void reset() {
        ((JavascriptExecutor)webDriver).executeScript("window.transactionRecorder.reset()");
    }

    public List<TransactionEvent> getTransactionEvents() {
        String transactionEventsJson = (String)((JavascriptExecutor)webDriver)
                .executeScript("return window.transactionRecorder.getTransactionEventsJson()");
        LOGGER.info("Retrieved {}", transactionEventsJson);

        TransactionEventCollectionWrapper transactionEventCollectionWrapper;
        try {
            transactionEventCollectionWrapper = objectMapper.readValue(
                    transactionEventsJson,
                    TransactionEventCollectionWrapper.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        LOGGER.info("Read as {} events", transactionEventCollectionWrapper.events.size());

        return transactionEventCollectionWrapper.events;
    }
}
