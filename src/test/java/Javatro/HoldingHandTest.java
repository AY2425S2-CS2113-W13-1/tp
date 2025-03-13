package Javatro;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javatro_core.Card;

import org.junit.jupiter.api.Test;

import java.util.List;

public class HoldingHandTest {

    @Test
    void testDeck() {
        Deck deck = new Deck();
        int cardsRemaining = deck.getRemainingCards();
        assertEquals(52, cardsRemaining);
        Card drawOne = deck.draw();
        assertEquals(51, deck.getRemainingCards());
    }

    @Test
    void testHoldingHand() {
        Deck deck = new Deck();
        HoldingHand holdingHand = new HoldingHand();
        for (int i = 0; i < 8; i++) {
            holdingHand.add(deck.draw());
        }
        int cardsRemaining = deck.getRemainingCards();
        assertEquals(44, cardsRemaining);
        holdingHand.discard(List.of(1, 3, 5), deck);
        assertEquals(41, deck.getRemainingCards());
        List<Card> playedHand = holdingHand.play(List.of(1, 4, 5), deck);
        assertEquals(3, playedHand.size());
        assertEquals(38, deck.getRemainingCards());
    }
}
