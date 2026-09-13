package screens;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

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

    protected boolean isPresent(By locator) {
        return !driver.findElements(locator).isEmpty();
    }

    protected void swipeElementHorizontally(WebElement webElement, boolean toRight) {
        int centerY = webElement.getRect().getY() + (webElement.getRect().getHeight() / 2);
        int screenWidth = driver.manage().window().getSize().getWidth();

        int startX = toRight ? (int) (screenWidth * 0.15) : (int) (screenWidth * 0.85);
        int endX = toRight ? (int) (screenWidth * 0.90) : (int) (screenWidth * 0.10);

        int steps = 10;
        int stepDurationMs = 40;

        PointerInput dedito = new PointerInput(PointerInput.Kind.TOUCH, "dedito");
        Sequence sequence = new Sequence(dedito, 0)
                .addAction(dedito.createPointerMove(Duration.ofSeconds(0), PointerInput.Origin.viewport(), startX, centerY))
                .addAction(dedito.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(new Pause(dedito, Duration.ofMillis(125)));

        for (int i = 1; i <= steps; i++) {
            int intermediateX = startX + ((endX - startX) * i / steps);
            sequence.addAction(dedito.createPointerMove(Duration.ofMillis(stepDurationMs), PointerInput.Origin.viewport(), intermediateX, centerY));
        }

        sequence.addAction(new Pause(dedito, Duration.ofMillis(100)));
        sequence.addAction(dedito.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(List.of(sequence));
    }

    protected void swipeElementVertically(WebElement webElement, boolean up) {
        int centerX = webElement.getRect().getX() + (webElement.getRect().getWidth() / 2);
        int centerY = webElement.getRect().getY() + (webElement.getRect().getHeight() / 2);
        int endY = up ? centerY - 900 : centerY + 900;

        PointerInput dedito = new PointerInput(PointerInput.Kind.TOUCH, "dedito");
        Sequence sequence = new Sequence(dedito, 0)
                .addAction(dedito.createPointerMove(Duration.ofSeconds(0), PointerInput.Origin.viewport(), centerX, centerY))
                .addAction(dedito.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(new Pause(dedito, Duration.ofMillis(125)))
                .addAction(dedito.createPointerMove(Duration.ofSeconds(2), PointerInput.Origin.viewport(), centerX, endY))
                .addAction(dedito.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(List.of(sequence));
    }
}