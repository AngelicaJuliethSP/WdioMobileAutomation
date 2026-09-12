package screens;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Map;

public abstract class BaseScreen {

    protected AndroidDriver driver;
    protected WebDriverWait wait;

    public BaseScreen(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    protected WebElement waitForVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected void tap(By locator) {
        waitForVisible(locator).click();
    }

    protected String getText(By locator) {
        return waitForVisible(locator).getText();
    }

    protected boolean isDisplayed(By locator) {
        try {
            return waitForVisible(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    protected void swipe(WebElement element, String direction, double percent) {
        driver.executeScript("mobile: swipeGesture", Map.of(
                "elementId", ((org.openqa.selenium.remote.RemoteWebElement) element).getId(),
                "direction", direction,
                "percent", percent
        ));
    }

    protected boolean isPresent(By locator) {
        return !driver.findElements(locator).isEmpty();
    }
}