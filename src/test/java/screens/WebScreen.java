package screens;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

public class WebScreen extends BaseScreen {

    private final By screenContainer = By.className("android.webkit.WebView");

    private final By webdriverIOText = AppiumBy.androidUIAutomator(
            "new UiSelector().textContains(\"WebdriverIO\")");

    public WebScreen(AndroidDriver driver) {
        super(driver);
    }

    public boolean isScreenDisplayed() {
        return isDisplayed(screenContainer);
    }

    public boolean isWebdriverIOTextDisplayed() {
        // La WebView aparece antes de que su contenido renderice en el
        // emulador de CI (renderizado por software); esperar el texto.
        return waitForDisplayed(webdriverIOText, java.time.Duration.ofSeconds(20));
    }
}