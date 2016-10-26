package me.loki2302.webdriver;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.loki2302.spring.advanced.TransactionEvent;
import me.loki2302.spring.advanced.TransactionEventCollectionWrapper;
import me.loki2302.spring.advanced.TransactionFrame;
import me.loki2302.spring.advanced.TransactionFrameBuilder;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class FrontEndTransactionFacade {
    private final static Logger LOGGER = LoggerFactory.getLogger(FrontEndTransactionFacade.class);

    @Autowired
    private WebDriver webDriver;

    @Autowired
    private ObjectMapper objectMapper;

    public void reset() {
        ((JavascriptExecutor)webDriver).executeScript("window.transactionRecorder.reset()");
    }

    public TransactionFrame getTransactionEvents() {
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

        TransactionFrameBuilder transactionFrameBuilder = new TransactionFrameBuilder();
        for(TransactionEvent transactionEvent : transactionEventCollectionWrapper.events) {
            transactionFrameBuilder.handleTransactionEvent(transactionEvent);
        }

        return transactionFrameBuilder.getRootTransactionFrame();
    }
}
