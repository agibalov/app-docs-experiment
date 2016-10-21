package me.loki2302.webdriver;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverEventListener;
import org.springframework.beans.factory.annotation.Autowired;

public class SynchronizingWebDriverEventListener implements WebDriverEventListener {
    @Autowired
    private Angular2Synchronizer angular2Synchronizer;

    @Override
    public void beforeNavigateTo(String url, WebDriver driver) {
    }

    @Override
    public void afterNavigateTo(String url, WebDriver driver) {
        angular2Synchronizer.synchronizeAngular2Smart(driver);
    }

    @Override
    public void beforeNavigateBack(WebDriver driver) {
    }

    @Override
    public void afterNavigateBack(WebDriver driver) {
    }

    @Override
    public void beforeNavigateForward(WebDriver driver) {
    }

    @Override
    public void afterNavigateForward(WebDriver driver) {
    }

    @Override
    public void beforeNavigateRefresh(WebDriver driver) {
    }

    @Override
    public void afterNavigateRefresh(WebDriver driver) {
    }

    @Override
    public void beforeFindBy(By by, WebElement element, WebDriver driver) {
    }

    @Override
    public void afterFindBy(By by, WebElement element, WebDriver driver) {
    }

    @Override
    public void beforeClickOn(WebElement element, WebDriver driver) {
    }

    @Override
    public void afterClickOn(WebElement element, WebDriver driver) {
        angular2Synchronizer.synchronizeAngular2Smart(driver);
    }

    @Override
    public void beforeChangeValueOf(WebElement element, WebDriver driver) {
    }

    @Override
    public void afterChangeValueOf(WebElement element, WebDriver driver) {
        angular2Synchronizer.synchronizeAngular2Smart(driver);
    }

    @Override
    public void beforeScript(String script, WebDriver driver) {
    }

    @Override
    public void afterScript(String script, WebDriver driver) {
    }

    @Override
    public void onException(Throwable throwable, WebDriver driver) {
        // TODO: check if I should rethrow
    }
}
