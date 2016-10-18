package me.loki2302.webdriver;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.util.StreamUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class WebDriverUtils {
    private final static Logger LOGGER = LoggerFactory.getLogger(WebDriverUtils.class);

    @Autowired
    private WebDriver webDriver;

    @Value("classpath:/angular2-sync.js")
    private Resource angular2SyncScript;

    public File makeScreenshot() {
        return ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
    }

    public void synchronizeAngular2() {
        String scriptContent = readResource(angular2SyncScript);
        ((JavascriptExecutor)webDriver).executeAsyncScript(scriptContent);
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
