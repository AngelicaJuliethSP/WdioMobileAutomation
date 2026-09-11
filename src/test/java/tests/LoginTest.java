package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import screens.HomeScreen;
import screens.LoginScreen;
import utils.DataGenerator;

public class LoginTest extends BaseTest {

    @Test
    public void successfulLogin() {
        HomeScreen homeScreen = new HomeScreen(driver);
        homeScreen.tapLoginIcon();

        LoginScreen loginScreen = new LoginScreen(driver);
        String email = DataGenerator.generateRandomEmail();
        String password = "Test1234!";

        // Reusa el flujo de signup para garantizar un usuario válido y
        // fresco, sin depender de que SignupTest se haya ejecutado antes
        // ni de un usuario fijo hardcodeado.
        loginScreen.submitSignUp(email, password);
        loginScreen.tapDialogOk();

        loginScreen.submitLogin(email, password);

        String actualMessage = loginScreen.getDialogMessage();
        Assert.assertEquals(actualMessage, "You are logged in!",
                "El mensaje de login exitoso no coincide");

        loginScreen.tapDialogOk();
    }
}