package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import screens.HomeScreen;
import screens.SwipeScreen;

public class SwipeTest extends BaseTest {

    @Test
    public void swipeCardsAndFindHiddenText() {
        HomeScreen homeScreen = new HomeScreen(driver);
        homeScreen.tapSwipeIcon();

        SwipeScreen swipeScreen = new SwipeScreen(driver);
        Assert.assertTrue(swipeScreen.isScreenDisplayed(), "Swipe screen no se muestra");

        // b. Swipe de la primera carta y verificar que se oculta
        Assert.assertTrue(swipeScreen.isCardIndexVisible(0), "La carta 0 debería estar visible al inicio");
        swipeScreen.swipeCarouselForward();
        Assert.assertFalse(swipeScreen.isCardIndexVisible(0), "La carta 0 debería ocultarse tras el swipe");

        // c. Seguir hasta la última carta (índice 5, ya que hay 6 en total)
        for (int i = 0; i < 4; i++) {
            swipeScreen.swipeCarouselForward();
        }
        Assert.assertTrue(swipeScreen.isCardIndexVisible(5), "La última carta (5) debería estar visible");
        Assert.assertFalse(swipeScreen.isCardIndexVisible(4), "No debería verse ninguna carta anterior junto a la última");

        // d. Scroll vertical hasta encontrar el texto oculto
        swipeScreen.scrollUntilFoundMeVisible(10);
        Assert.assertTrue(swipeScreen.isFoundMeTextDisplayed(),
                "No se encontró el texto 'You found me!!!' tras el scroll vertical");
    }
}