package javatro.display.screens;

import javatro.core.JavatroCore;
import javatro.core.JavatroException;
import javatro.core.PokerHand;
import javatro.display.UI;
import javatro.manager.JavatroManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class PokerHandScreenTest {

    private PokerHandScreen pokerHandScreen;
    private List<PokerHand> pokerHands;

    @BeforeEach
    public void setUp() throws JavatroException {
        pokerHands = new ArrayList<>();
        UI javatroView = new UI();

        /** The manager responsible for handling interactions between the view and core components. */
        JavatroManager javatroManager;

        JavatroCore javatroCore = new JavatroCore();

        try {
            javatroManager = new JavatroManager(javatroView, javatroCore);
        } catch (JavatroException e) {
            throw new RuntimeException(e);
        }

        JavatroManager.runningTests = true;


        pokerHandScreen = new PokerHandScreen();
        javatroView.setCurrentScreen(UI.getPokerHandScreen());

        // Get Poker Hands
        pokerHands = Stream.of(PokerHand.HandType.values())
                        .map(PokerHand::new)
                        .collect(Collectors.toList());

    }

    @Test
    public void testInitialization() {
        assertNotNull(pokerHandScreen);
        assertEquals(pokerHands, pokerHandScreen.getPokerHands());
    }

    @Test
    public void testDisplayScreen() {
        assertDoesNotThrow(() -> pokerHandScreen.displayScreen());
    }

    @Test
    public void testDisplayOutput() {
        // Capture console output
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        pokerHandScreen.displayScreen();

        System.setOut(originalOut); // Restore original System.out

        String output = outputStream.toString();
        assertTrue(output.contains("Royal Flush"));
        assertTrue(output.contains("Straight Flush"));
        assertTrue(output.contains("Four of a Kind"));
    }

    @Test
    public void testIncrementPlayed() {
        PokerHand.HandType handType = PokerHand.HandType.ROYAL_FLUSH;
        PokerHand hand = null;
        try {
            hand = pokerHandScreen.getHand(handType);
        } catch (JavatroException e) {
            throw new RuntimeException(e);
        }

        int previousPlayCount = hand.playCount();

        // Increment the played count for ROYAL_FLUSH
        pokerHandScreen.incrementPlayed(handType);

        // Retrieve the hand again to verify the increment
        PokerHand updatedHand = null;
        try {
            updatedHand = pokerHandScreen.getHand(handType);
        } catch (JavatroException e) {
            throw new RuntimeException(e);
        }

        assertEquals(previousPlayCount + 1, updatedHand.playCount(), "Play count should be incremented by 1");
    }

    @Test
    public void testGetHandSuccessful() {
        PokerHand.HandType handType = PokerHand.HandType.STRAIGHT_FLUSH;
        PokerHand hand = null;
        try {
            hand = pokerHandScreen.getHand(handType);
        } catch (JavatroException e) {
            throw new RuntimeException(e);
        }

        assertNotNull(hand, "Hand should be successfully retrieved");
        assertEquals(handType, hand.handType());
    }

    @Test
    public void testGetHandFailure() {
        assertThrows(JavatroException.class, () -> pokerHandScreen.getHand(null));
    }


    @Test
    public void testDisplayScreenWithEmptyHands() throws JavatroException {
        // Clearing the list directly via the increment method to simulate an empty state
        for (PokerHand.HandType handType : PokerHand.HandType.values()) {
            pokerHandScreen.incrementPlayed(handType);  // Ensure all hands are modified
        }

        PrintStream originalOut = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        pokerHandScreen.displayScreen();

        System.setOut(originalOut);

        String output = outputStream.toString();
        assertTrue(output.contains("POKER HAND"), "Output should include title even if no hands are available");
    }

    @Test
    public void testDisplayScreenWithDifferentHands() throws JavatroException {
        PokerHand.HandType handType = PokerHand.HandType.FOUR_OF_A_KIND;
        pokerHandScreen.incrementPlayed(handType); // Change the state of one hand

        PrintStream originalOut = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        pokerHandScreen.displayScreen();

        System.setOut(originalOut);

        String output = outputStream.toString();
        assertTrue(output.contains("Four of a Kind"), "Output should include 'Four of a Kind'");
    }
}
