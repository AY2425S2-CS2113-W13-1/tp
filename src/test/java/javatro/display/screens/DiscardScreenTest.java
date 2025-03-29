package javatro.display.screens;

import static org.junit.jupiter.api.Assertions.*;

import javatro.core.Card;
import javatro.core.Deck;
import javatro.core.JavatroCore;
import javatro.core.JavatroException;

import javatro.display.UI;
import javatro.manager.JavatroManager;
import javatro.manager.options.Option;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;


public class DiscardScreenTest {

    private DiscardScreen discardScreen;
    private List<Card> holdingHand;

    @BeforeEach
    public void setUp() throws JavatroException {
        discardScreen = new DiscardScreen();
        holdingHand = new ArrayList<>();
        UI javatroView = new UI();
        JavatroCore javatroCore = new JavatroCore();

        // Initialize the deck and draw cards for the hand
        Deck deck = new Deck();
        for (int i = 0; i < 5; i++) {
            holdingHand.add(deck.draw());
        }

        JavatroManager javatroManager;

        try {
            javatroManager = new JavatroManager(javatroView, javatroCore);
        } catch (JavatroException e) {
            throw new RuntimeException(e);
        }

        JavatroManager.runningTests = true;
        JavatroManager.beginGame();
    }


    @Test
    public void testInitialization() {
        assertNotNull(discardScreen);
        assertEquals("\u001B[1m::: SELECT CARDS TO DISCARD :::\u001B[0m", discardScreen.getOptionsTitle());
    }

    @Test
    public void testCommandMap() {
        assertNotNull(discardScreen.getCommandMap());
        assertEquals(5, discardScreen.getCommandMap().size(), "DiscardScreen should have exactly 5 options in the command map");

        List<String> commandDescriptions = discardScreen.getCommandMap().stream()
            .map(Option::getDescription)
            .toList();

        // Check for all expected commands
        assertTrue(commandDescriptions.contains("Select Cards"), "Command map should contain 'Select Cards' option");
        assertTrue(commandDescriptions.contains("Sort cards by Suit"), "Command map should contain 'Sort by Suit' option");
        assertTrue(commandDescriptions.contains("Sort cards by Rank"), "Command map should contain 'Sort by Rank' option");
        assertTrue(commandDescriptions.contains("View Poker Hands"), "Command map should contain 'View Poker Hands' option");
        assertTrue(commandDescriptions.contains("Return To Game"), "Command map should contain 'Resume Game' option");
    }

    @Test
    public void testPokerHandOptionExecution() throws JavatroException {
        Option pokerHandOption = discardScreen.getCommandMap().get(3); // PokerHandOption

        assertNotNull(pokerHandOption);
        pokerHandOption.execute();

        // Assuming this command leads to PokerHandScreen
        assertTrue(UI.getCurrentScreen() instanceof PokerHandScreen);
    }


}
