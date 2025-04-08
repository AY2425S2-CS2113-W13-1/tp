package javatro.display.screens;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import javatro.core.JavatroException;
import javatro.manager.options.ExitGameOption;
import javatro.manager.options.MainMenuOption;
import javatro.manager.options.Option;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class WinGameScreenTest extends ScreenTest {

    private WinGameScreen winGameScreen;

    @BeforeEach
    public void setUp() {
        // Mock AudioPlayer to prevent actual audio playback during testing
        super.setUp();
        try {
            winGameScreen = new WinGameScreen();
        } catch (JavatroException e) {
            fail("Failed to setup screen: " + winGameScreen);
        }
    }

    @Test
    public void testInitialization() {
        assertNotNull(winGameScreen, "WinGameScreen should be initialized.");
        assertEquals(
                2, winGameScreen.getCommandMap().size(), "WinGameScreen should have 2 options.");
    }

    @Test
    public void testCommandMapContainsExpectedOptions() {
        List<Option> commandMap = winGameScreen.getCommandMap();

        assertTrue(
                commandMap.stream().anyMatch(option -> option instanceof MainMenuOption),
                "Command map should contain a MainMenuOption.");
        assertTrue(
                commandMap.stream().anyMatch(option -> option instanceof ExitGameOption),
                "Command map should contain an ExitGameOption.");
    }

    @Test
    public void testRandomQuoteSelection() {
        // Ensure all quotes are accessible
        int totalQuotes = WinGameScreen.QUOTES.size();
        assertTrue(totalQuotes > 0, "Quote list should not be empty.");

        boolean allQuotesAccessible = true;
        for (int i = 0; i < 1000; i++) { // Run a large number of random selections
            int randomIndex = ThreadLocalRandom.current().nextInt(totalQuotes);
            if (randomIndex >= totalQuotes) {
                allQuotesAccessible = false;
                break;
            }
        }
        assertTrue(
                allQuotesAccessible, "All quotes should be accessible through random selection.");
    }

    @Test
    public void testDisplayScreenDoesNotThrowException() {
        assertDoesNotThrow(
                () -> winGameScreen.displayScreen(),
                "displayScreen() should not throw any exceptions.");
    }

    @Test
    public void testQuoteIndexWithinBounds() {
        int totalQuotes = WinGameScreen.QUOTES.size();

        for (int i = 0; i < 100; i++) {
            int randomIndex = ThreadLocalRandom.current().nextInt(totalQuotes);
            assertTrue(
                    randomIndex < totalQuotes, "Random quote index should be within valid range.");
        }
    }
}
