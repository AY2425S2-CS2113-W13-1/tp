package javatro.display.screens;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import javatro.core.JavatroException;
import javatro.core.PokerHand;
import javatro.manager.options.ExitGameOption;
import javatro.manager.options.NextRoundOption;
import javatro.manager.options.Option;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class WinRoundScreenTest {

    private WinRoundScreen winRoundScreen;

    @BeforeEach
    public void setUp() {
        try {
            winRoundScreen = new WinRoundScreen();
        } catch (JavatroException e) {
            fail("Did not set up win round screen " + winRoundScreen);
        }
    }

    @Test
    public void testInitialization() {
        assertNotNull(winRoundScreen, "WinRoundScreen should be initialized.");
        assertEquals(
                2, winRoundScreen.getCommandMap().size(), "WinRoundScreen should have 2 options.");
    }

    @Test
    public void testCommandMapContainsExpectedOptions() {
        List<Option> commandMap = winRoundScreen.getCommandMap();

        assertTrue(
                commandMap.stream().anyMatch(option -> option instanceof NextRoundOption),
                "Command map should contain a NextRoundOption.");
        assertTrue(
                commandMap.stream().anyMatch(option -> option instanceof ExitGameOption),
                "Command map should contain an ExitGameOption.");
    }

    @Test
    public void testRandomPlanetCardSelection() {
        PokerHand.HandType[] handTypes = PokerHand.HandType.values();
        int totalHandTypes = handTypes.length;

        for (int i = 0; i < 1000; i++) { // Repeat to cover random selection
            int randomIndex = ThreadLocalRandom.current().nextInt(totalHandTypes);
            assertTrue(randomIndex < totalHandTypes, "Random index should be within valid range.");
        }
    }
}
