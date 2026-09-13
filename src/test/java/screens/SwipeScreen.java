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

    private By cardAtIndex(int index) {
        return By.xpath("//android.view.ViewGroup[@resource-id='__CAROUSEL_ITEM_" + index + "__']/android.view.ViewGroup[@content-desc='card']");
    }

    public boolean isCardIndexVisible(int index) {
        return isPresent(cardAtIndex(index));
    }

    public void swipeCarouselForward() {
        List<WebElement> cards = driver.findElements(card);
        if (!cards.isEmpty()) {
            // Deslizar de derecha a izquierda avanza el carrusel a la
            // siguiente carta (hacia la derecha no hacía nada porque
            // ya estábamos en la primera carta, no había hacia dónde retroceder).
            swipeElementHorizontally(cards.get(0), false);
        }
    }

    public void swipeScreenUp() {
        WebElement container = driver.findElement(screenContainer);
        swipeElementVertically(container, true);
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