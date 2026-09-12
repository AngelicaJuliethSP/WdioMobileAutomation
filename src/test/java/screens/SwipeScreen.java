package screens;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class SwipeScreen extends BaseScreen {

    private final By screenContainer = AppiumBy.accessibilityId("Swipe-screen");
    private final By card = AppiumBy.accessibilityId("card");
    private final By foundMeText = By.xpath("//android.widget.TextView[@text='You found me!!!']");

    public SwipeScreen(AndroidDriver driver) {
        super(driver);
    }

    public boolean isScreenDisplayed() {
        return isDisplayed(screenContainer);
    }

    public int getVisibleCardCount() {
        return driver.findElements(card).size();
    }

    public void swipeTopCardRight() {
        List<WebElement> cards = driver.findElements(card);
        if (!cards.isEmpty()) {
            swipe(cards.get(0), "right", 1.0);
        }
    }

    public void swipeAllCardsExceptLast() {
        while (getVisibleCardCount() > 1) {
            swipeTopCardRight();
        }
    }

    public void swipeScreenUp() {
        WebElement container = driver.findElement(screenContainer);
        swipe(container, "up", 0.75);
    }

    public void scrollUntilFoundMeVisible(int maxAttempts) {
        int attempts = 0;
        while (!isPresent(foundMeText) && attempts < maxAttempts) {
            swipeScreenUp();
            attempts++;
        }
    }

    public boolean isFoundMeTextDisplayed() {
        return isDisplayed(foundMeText);
    }
}