package me.loki2302.webdriver;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;

public class Angular2Synchronizer {
    private final static Logger LOGGER = LoggerFactory.getLogger(Angular2Synchronizer.class);
    private final static int MAX_ANGULAR2_SYNC_ITERATIONS = 100;

    @Value("classpath:/angular2-sync.js")
    private Resource angular2SyncScript;

    public void synchronize(WebDriver webDriver) {
        String scriptContent = readResource(angular2SyncScript);

        for(int i = 0; i < MAX_ANGULAR2_SYNC_ITERATIONS; ++i) {
            boolean didWork = (Boolean) ((JavascriptExecutor) webDriver).executeAsyncScript(scriptContent);
            boolean shouldSyncOneMoreTime = didWork;
            LOGGER.info("Attempt #{}, shouldSyncOneMoreTime={}", i, shouldSyncOneMoreTime);
            if(shouldSyncOneMoreTime) {
                continue;
            }

            LOGGER.info("Should be stable now");
            break;
        }
    }

    private static String readResource(Resource resource) {
        try {
            String scriptContent = StreamUtils.copyToString(resource.getInputStream(), Charset.forName("UTF-8"));
            LOGGER.info("Read {} as {}", resource, scriptContent);
            return scriptContent;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
