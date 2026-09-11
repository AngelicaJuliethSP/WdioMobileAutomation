package screens;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

public class HomeScreen extends BaseScreen {

    private final By homeIcon = AppiumBy.accessibilityId("Home");;
    private final By loginIcon = AppiumBy.accessibilityId("Login");
    private final By formsIcon = AppiumBy.accessibilityId("Forms");
    private final By swipeIcon = AppiumBy.accessibilityId("Swipe");

    private final By menuIcon = By.xpath("//android.widget.TextView[@text='Home']");

    public HomeScreen(AndroidDriver driver) {
        super(driver);
    }

    public void tapHomeIcon() {
        tap(homeIcon);
    }

    public void tapLoginIcon() {
        tap(loginIcon);
    }

    public void tapFormsIcon() {
        tap(formsIcon);
    }

    public void tapSwipeIcon() {
        tap(swipeIcon);
    }

    public void tapMenuIcon() {
        tap(menuIcon);
    }

    public boolean isHomeIconDisplayed() {
        return isDisplayed(homeIcon);
    }
}
