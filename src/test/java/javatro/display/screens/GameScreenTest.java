package javatro.display.screens;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import javatro.core.Card;
import javatro.core.JavatroException;
import javatro.manager.options.DeckViewOption;
import javatro.manager.options.DiscardCardOption;
import javatro.manager.options.ExitGameOption;
import javatro.manager.options.MainMenuOption;
import javatro.manager.options.PlayCardOption;
import javatro.manager.options.PokerHandOption;
import javatro.storage.Storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

public class GameScreenTest extends ScreenTest {

    private GameScreen gameScreen;
    private Storage storage;

    @BeforeEach
    public void setUp() {
        super.setUp();
        storage = Storage.getStorageInstance();
        Storage.saveActive = false;

        try {
            gameScreen = new GameScreen();
        } catch (JavatroException e) {
            fail("Error creating the new game screen: " + e);
        }
    }

    @Test
    public void testCommandMapInitialization() {
        assertEquals(6, gameScreen.getCommandMap().size(), "Command map should contain 6 options.");
        assertInstanceOf(PlayCardOption.class, gameScreen.getCommandMap().get(0));
        assertInstanceOf(DiscardCardOption.class, gameScreen.getCommandMap().get(1));
        assertInstanceOf(PokerHandOption.class, gameScreen.getCommandMap().get(2));
        assertInstanceOf(DeckViewOption.class, gameScreen.getCommandMap().get(3));
        assertInstanceOf(MainMenuOption.class, gameScreen.getCommandMap().get(4));
        assertInstanceOf(ExitGameOption.class, gameScreen.getCommandMap().get(5));
    }

    @Test
    public void testPropertyChangeRoundName() {
        PropertyChangeEvent event = new PropertyChangeEvent(this, "roundName", "", "TEST ROUND");
        gameScreen.propertyChange(event);

        assertEquals("TEST ROUND", gameScreen.roundName, "Round name should be updated.");
    }

    @Test
    public void testPropertyChangeRoundDescription() {
        PropertyChangeEvent event =
                new PropertyChangeEvent(
                        this, "roundDescription", "", "This is a test round description.");
        gameScreen.propertyChange(event);

        assertEquals(
                "This is a test round description.",
                gameScreen.roundDescription,
                "Round description should be updated.");
    }

    @Test
    public void testPropertyChangeBlindScore() {
        PropertyChangeEvent event = new PropertyChangeEvent(this, "blindScore", 0, 100);
        gameScreen.propertyChange(event);

        assertEquals(100, GameScreen.blindScore, "Blind score should be updated.");
    }

    @Test
    public void testPropertyChangeRoundScore() {
        PropertyChangeEvent event = new PropertyChangeEvent(this, "currentScore", 0L, 500L);
        gameScreen.propertyChange(event);

        assertEquals(500L, GameScreen.roundScore, "Round score should be updated.");
    }

    @Test
    public void testPropertyChangeHandsLeft() {
        PropertyChangeEvent event = new PropertyChangeEvent(this, "remainingPlays", 0, 5);
        gameScreen.propertyChange(event);

        assertEquals(5, GameScreen.handsLeft, "Hands left should be updated.");
    }

    @Test
    public void testPropertyChangeDiscardsLeft() {
        PropertyChangeEvent event = new PropertyChangeEvent(this, "remainingDiscards", 0, 3);
        gameScreen.propertyChange(event);

        assertEquals(3, GameScreen.discardsLeft, "Discards left should be updated.");
    }

    @Test
    public void testPropertyChangeHoldingHand() {
        List<Card> holdingHand = new ArrayList<>();
        holdingHand.add(new Card(Card.Rank.ACE, Card.Suit.SPADES));
        holdingHand.add(new Card(Card.Rank.KING, Card.Suit.HEARTS));
        holdingHand.add(new Card(Card.Rank.QUEEN, Card.Suit.DIAMONDS));
        holdingHand.add(new Card(Card.Rank.JACK, Card.Suit.CLUBS));
        holdingHand.add(new Card(Card.Rank.TEN, Card.Suit.SPADES));
        holdingHand.add(new Card(Card.Rank.NINE, Card.Suit.HEARTS));
        holdingHand.add(new Card(Card.Rank.EIGHT, Card.Suit.DIAMONDS));
        holdingHand.add(new Card(Card.Rank.SEVEN, Card.Suit.CLUBS));

        PropertyChangeEvent event = new PropertyChangeEvent(this, "holdingHand", null, holdingHand);
        gameScreen.propertyChange(event);

        assertEquals(8, GameScreen.holdingHand.size(), "Holding hand should contain 8 cards.");
        assertEquals(
                Card.Rank.ACE,
                GameScreen.holdingHand.get(0).rank(),
                "First card rank should be ACE.");
        assertEquals(
                Card.Suit.SPADES,
                GameScreen.holdingHand.get(0).suit(),
                "First card suit should be SPADES.");
    }

    @Test
    public void testPropertyChangeInvalidProperty() {
        PropertyChangeEvent event = new PropertyChangeEvent(this, "invalidProperty", null, null);
        gameScreen.propertyChange(event);

        // No exception should be thrown for an invalid property change
        assertTrue(true);
    }
}
