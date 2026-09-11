package screens;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

public class LoginScreen extends BaseScreen {

    private final By emailInput = AppiumBy.accessibilityId("input-email");
    private final By passwordInput = AppiumBy.accessibilityId("input-password");
    private final By confirmPasswordInput = AppiumBy.id("input-repeat-password");
    private final By loginButton = AppiumBy.accessibilityId("button-LOGIN");

    private final By signUpTab = By.xpath("//android.widget.TextView[@text='Sign up']");
    private final By loginTab = By.xpath("//android.widget.TextView[@text='Login']");
    private final By signUpButton = AppiumBy.androidUIAutomator(
            "new UiSelector().text('SIGN UP')");

    public LoginScreen(AndroidDriver driver) {
        super(driver);
    }

    public void switchToLoginTab() {
        tap(loginTab);
    }

    public void switchToSignUpTab() {
        tap(signUpTab);
    }

    public void enterEmail(String email) {
        driver.findElement(emailInput).sendKeys(email);
    }

    public void enterPassword(String password) {
        driver.findElement(passwordInput).sendKeys(password);
    }

    public void enterConfirmPassword(String confirmPassword) {
        waitForVisible(confirmPasswordInput).sendKeys(confirmPassword);
    }

    public void tapLogin() {
        tap(loginButton);
    }

    public void tapSignUpSubmit() {
        tap(signUpButton);
    }

    public boolean isEmailFieldDisplayed() {
        return isDisplayed(emailInput);
    }
}