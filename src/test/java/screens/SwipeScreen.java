package screens;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebElement;

import java.util.List;

public class SwipeScreen extends BaseScreen {

    private final By screenContainer = AppiumBy.accessibilityId("Swipe-screen");
    private final By card = AppiumBy.accessibilityId("card");
    private final By foundMeText = By.xpath("//*[@text='You found me!!!' or @content-desc='You found me!!!']");

    public SwipeScreen(AndroidDriver driver) {
        super(driver);
    }

    public boolean isScreenDisplayed() {
        // El tap al ícono responde antes de que la pantalla termine de montarse
        // (28 ms en CI); esperar en vez de consultar una sola vez.
        return waitForDisplayed(screenContainer, java.time.Duration.ofSeconds(15));
    }

    private By cardAtIndex(int index) {
        return By.xpath("//android.view.ViewGroup[@resource-id='__CAROUSEL_ITEM_" + index + "__']/android.view.ViewGroup[@content-desc='card']");
    }

    public boolean isCardIndexVisible(int index) {
        return isPresent(cardAtIndex(index));
    }

    public void waitUntilCardHidden(int index) {
        wait.until(driver -> !isCardIndexVisible(index));
    }

    public void swipeForwardAndWait(int currentIndex) {
        int attempts = 0;
        int maxAttempts = 3;

        while (isCardIndexVisible(currentIndex) && attempts < maxAttempts) {
            swipeCarouselForward();
            try {
                wait.until(driver -> !isCardIndexVisible(currentIndex));
                return;
            } catch (org.openqa.selenium.TimeoutException e) {
                attempts++;
            }
        }
        // Fallar explícitamente si la tarjeta sigue visible tras 3 intentos:
        // antes el método terminaba en silencio y el test continuaba con un
        // estado inconsistente (fallos confusos más adelante).
        if (isCardIndexVisible(currentIndex)) {
            throw new org.openqa.selenium.TimeoutException(
                    "La tarjeta " + currentIndex + " sigue visible después de "
                            + maxAttempts + " intentos de swipe.");
        }
    }

    public void swipeCarouselForward() {
        List<WebElement> cards = driver.findElements(card);
        if (!cards.isEmpty()) {
            // mobile: swipeGesture nativo de uiautomator2: el gesto W3C manual
            // (PointerInput) no mueve el carrusel en este emulador (600 ms no
            // genera la velocidad necesaria) y un percent alto (0.9) lo
            // interpreta como cambio de página y regresa al Home. percent 0.5
            // sobre el área de la carta avanza exactamente una posición.
            Rectangle cardRect = cards.get(0).getRect();
            driver.executeScript("mobile: swipeGesture", java.util.Map.of(
                    "left", cardRect.getX(),
                    "top", cardRect.getY(),
                    "width", cardRect.getWidth(),
                    "height", cardRect.getHeight(),
                    "direction", "left",
                    "percent", 0.5));
        }
    }

    public void scrollUntilFoundMeVisible(int maxAttempts) {
        try {
            // Usa el motor nativo de Android para hacer scroll hasta que el texto sea visible
            driver.findElement(AppiumBy.androidUIAutomator(
                    "new UiScrollable(new UiSelector().scrollable(true))" +
                            ".scrollIntoView(new UiSelector().text(\"You found me!!!\"))"
            ));
        } catch (Exception e) {
            // En caso de que falle UiScrollable, intentamos con swipe de respaldo
            int attempts = 0;
            while (!isDisplayed(foundMeText) && attempts < maxAttempts) {
                swipeScreenUp();
                attempts++;
            }
        }
    }

    public boolean isCardOnScreen(int index) {
        return isPartiallyOnScreen(cardAtIndex(index));
    }

    public boolean waitForCardOnScreen(int index, java.time.Duration timeout) {
        // Tras ocultar la carta anterior, el carrusel puede tardar en montar
        // la siguiente; verificar de inmediato daba falsos negativos.
        try {
            new org.openqa.selenium.support.ui.WebDriverWait(driver, timeout)
                    .until(driver -> isCardOnScreen(index));
            return true;
        } catch (org.openqa.selenium.TimeoutException e) {
            return false;
        }
    }

    public boolean isFoundMeTextDisplayed() {
        return isDisplayed(foundMeText);
    }
}