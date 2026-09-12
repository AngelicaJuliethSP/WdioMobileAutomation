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

        int initialCount = swipeScreen.getVisibleCardCount();
        swipeScreen.swipeTopCardRight();
        int countAfterOneSwipe = swipeScreen.getVisibleCardCount();
        Assert.assertEquals(countAfterOneSwipe, initialCount - 1,
                "La carta anterior no se ocultó tras el swipe");

        swipeScreen.swipeAllCardsExceptLast();
        Assert.assertEquals(swipeScreen.getVisibleCardCount(), 1,
                "Debería quedar solo una carta visible");

        swipeScreen.scrollUntilFoundMeVisible(10);
        Assert.assertTrue(swipeScreen.isFoundMeTextDisplayed(),
                "No se encontró el texto 'You found me!!!' tras el scroll vertical");
    }
}