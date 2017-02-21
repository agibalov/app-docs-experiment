package me.loki2302.webdriver;

import io.github.bonigarcia.wdm.ChromeDriverManager;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

@Configuration
public class WebDriverConfiguration {
    @Bean(destroyMethod = "quit")
    public WebDriver webDriver() {
        ChromeDriverManager.getInstance().setup();

        LoggingPreferences loggingPreferences = new LoggingPreferences();
        loggingPreferences.enable(LogType.BROWSER, Level.ALL);

        DesiredCapabilities desiredCapabilities = DesiredCapabilities.chrome();
        desiredCapabilities.setCapability(CapabilityType.LOGGING_PREFS, loggingPreferences);

        ChromeDriver chromeDriver = new ChromeDriver(desiredCapabilities);
        chromeDriver.manage().timeouts().setScriptTimeout(5, TimeUnit.SECONDS);
        chromeDriver.manage().window().setSize(new Dimension(1366, 768));

        EventFiringWebDriver eventFiringWebDriver = new EventFiringWebDriver(chromeDriver);
        eventFiringWebDriver.register(synchronizingWebDriverEventListener());

        return eventFiringWebDriver;
    }

    @Bean
    public WebDriverUtils webDriverUtils() {
        return new WebDriverUtils();
    }

    @Bean
    public ScreenshotStrategy screenshotStrategy() {
        /**
         * As of Chrome 56.0.2924.87 and ChromeDriver 2.27,
         * native screenshots do not work - they are always black images.
         * Probably here's this issue: https://bugs.chromium.org/p/chromedriver/issues/detail?id=1625
         * The only reliable workaround is to use AWT screenshots
         */
        //return new WebDriverScreenshotStrategy();
        return new AwtScreenshotStrategy();
    }

    @Bean
    public Angular2Synchronizer angular2Synchronizer() {
        return new Angular2Synchronizer();
    }

    @Bean
    public SynchronizingWebDriverEventListener synchronizingWebDriverEventListener() {
        return new SynchronizingWebDriverEventListener();
    }

    @Bean
    public FrontEndTransactionFacade frontEndTransactionFacade() {
        return new FrontEndTransactionFacade();
    }
}
