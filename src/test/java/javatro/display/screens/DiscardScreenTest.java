package javatro.display.screens;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import javatro.core.Card;
import javatro.core.JavatroException;
import javatro.core.Deck;
import javatro.display.UI;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class DiscardScreenTest {

    private DiscardScreen discardScreen;
    private List<Card> holdingHand;

    @BeforeEach
    public void setUp() throws JavatroException {
        discardScreen = new DiscardScreen();
        holdingHand = new ArrayList<>();

        // Initialize the deck and draw cards for the hand
        Deck deck = new Deck();
        for (int i = 0; i < 5; i++) {
            holdingHand.add(deck.draw());
        }
    }

    @Test
    public void testInitialization() {
        assertNotNull(discardScreen);
        assertEquals("\u001B[1m::: SELECT CARDS TO DISCARD :::\u001B[0m", discardScreen.getOptionsTitle());
    }

//    @Test
//    public void testValidSelection() {
//        assertDoesNotThrow(() -> discardScreen.selectCards(holdingHand.subList(0, 5)));
//    }

//    @Test
//    public void testDisplayScreen() {
//        assertDoesNotThrow(() -> discardScreen.displayScreen());
//    }
//
//    @Test
//    public void testRenderingOutput() {
//        PrintStream originalOut = System.out;
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outputStream));
//
//        discardScreen.displayScreen();
//
//        System.setOut(originalOut);
//
//        String output = outputStream.toString();
//        assertTrue(output.contains("SELECT CARDS TO DISCARD"));
//    }


}
