package javatro.core;

import static javatro.core.Card.Rank.JACK;
import static javatro.core.Card.Rank.KING;
import static javatro.core.Card.Rank.NINE;
import static javatro.core.Card.Rank.QUEEN;
import static javatro.core.Card.Rank.TEN;
import static javatro.core.Card.Suit.CLUBS;
import static javatro.core.Card.Suit.DIAMONDS;
import static javatro.core.Card.Suit.HEARTS;
import static javatro.core.Card.Suit.SPADES;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javatro.core.jokers.HeldJokers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ScoreTest {
    private static List<Card> playedCardList;
    private static HeldJokers heldJokers;
    private static HandResult handResult;
    private static PokerHand result;
    private static Card cardOne;
    private static Card cardTwo;
    private static Card cardThree;
    private static Card cardFour;
    private static Card cardFive;

    /** Initialize a test run. */
    @BeforeEach
    void init() throws JavatroException {
        heldJokers = new HeldJokers();
        playedCardList =
                List.of(
                        new Card(NINE, DIAMONDS),
                        new Card(TEN, HEARTS),
                        new Card(JACK, CLUBS),
                        new Card(QUEEN, SPADES),
                        new Card(KING, SPADES));
    }

    /** Test that a hand played with no jokers gives the correct score. */
    @Test
    void testScore() throws JavatroException {
        result = HandResult.evaluateHand(playedCardList);
        Score scoreObject = new Score();
        long finalScore = scoreObject.getScore(result, playedCardList, heldJokers);
        assertEquals(316, finalScore);
    }
}
