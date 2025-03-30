package javatro.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

/**
 * This class contains unit tests for the {@link Deck} class. It tests the initialisation and
 * drawing of cards, ensuring the deck contains the proper number of cards.
 */
public class DeckTest {

    private static Deck deck;

    /** Initialize a new deck for each test. */
    @BeforeEach
    void init() {
        deck = new Deck(Deck.DeckType.DEFAULT);
    }

    /**
     * Test that each Deck initialised has 52 cards, and drawing 1 card gives the correct number of
     * cards remaining.
     */
    @Test
    void testDeck() throws JavatroException {
        int cardsRemaining = deck.getRemainingCards();
        assertEquals(52, cardsRemaining);
        Card drawOne = deck.draw();
        assertEquals(51, deck.getRemainingCards());
    }

    /** Test that a Checkered Deck has 52 cards, with 26 cards of Hearts, 26 cards of Spades. */
    @Test
    void testCheckeredDeck() throws JavatroException {
        Deck checkeredDeck = new Deck(Deck.DeckType.CHECKERED);
        int cardsRemaining = checkeredDeck.getRemainingCards();
        assertEquals(52, cardsRemaining);

        ArrayList<Card> cardArrayList = new ArrayList<Card>();
        for (int i = 0; i < 52; i++) {
            Card card = checkeredDeck.draw();
            assertNotEquals(Card.Suit.CLUBS, card.suit());
            assertNotEquals(Card.Suit.DIAMONDS, card.suit());
        }
    }

    /** Test that a Abandoned Deck has 40 cards, with no Face Cards. */
    @Test
    void testAbandonedDeck() throws JavatroException {
        Deck checkeredDeck = new Deck(Deck.DeckType.ABANDONED);
        int cardsRemaining = checkeredDeck.getRemainingCards();
        assertEquals(40, cardsRemaining);
        ArrayList<Card> cardArrayList = new ArrayList<Card>();
        for (int i = 0; i < 40; i++) {
            Card card = checkeredDeck.draw();
            assertNotEquals(Card.Rank.KING, card.suit());
            assertNotEquals(Card.Rank.QUEEN, card.suit());
            assertNotEquals(Card.Rank.JACK, card.suit());
        }
    }
}
