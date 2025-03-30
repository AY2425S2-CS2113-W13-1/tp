package javatro.core.jokers;

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
import static org.junit.jupiter.api.Assertions.fail;

import javatro.core.Card;
import javatro.core.HandResult;
import javatro.core.JavatroException;
import javatro.core.PokerHand;
import javatro.core.Score;
import javatro.core.jokers.addmult.AbstractJoker;
import javatro.core.jokers.addmult.GluttonousJoker;
import javatro.core.jokers.addmult.GreedyJoker;
import javatro.core.jokers.addmult.LustyJoker;
import javatro.core.jokers.addmult.WrathfulJoker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class HeldJokersTest {
    private static List<Card> playedCardList;
    private static HeldJokers heldJokers;
    private static PokerHand result;
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

    /**
     * Test that a normal HeldJokers cannot add more than 5 Jokers without exceeding the maximum
     * limit.
     */
    @Test
    void testIllegalAdd() {
        Joker gluttonousJoker = new GluttonousJoker();
        try {
            for (int i = 0; i < 10; i++) {
                heldJokers.add(gluttonousJoker);
            }
            fail();
        } catch (JavatroException e) {
            assertEquals(JavatroException.exceedsMaxJokers().getMessage(), e.getMessage());
        }
    }

    /** Test that a normal HeldJokers cannot delete a Joker at illegal positions */
    @Test
    void testIllegalDelete() {
        Joker gluttonousJoker = new GluttonousJoker();
        try {
            heldJokers.add(gluttonousJoker);
            heldJokers.remove(0);
            heldJokers.remove(1);
            fail();
        } catch (JavatroException e) {
            assertEquals(JavatroException.indexOutOfBounds(1).getMessage(), e.getMessage());
        }
    }

    /**
     * Test that a hand played triggers multiple joker effects correctly and has the right score.
     */
    void assertScoreEquals(HeldJokers currentJokers, int expectedScore) throws JavatroException {
        result = HandResult.evaluateHand(playedCardList);
        Score scoreObject = new Score();
        long finalScore = scoreObject.getScore(result, playedCardList, currentJokers);
        assertEquals(expectedScore, finalScore);
    }

    /**
     * Test that a hand played triggers multiple joker effects correctly and has the right score.
     */
    @Test
    void testMultipleJokersSuite() throws JavatroException {
        Joker gluttonousJoker = new GluttonousJoker();
        Joker counterJoker = new AbstractJoker();
        Joker wrathfulJoker = new WrathfulJoker();
        Joker lustyJoker = new LustyJoker();
        Joker greedyJoker = new GreedyJoker();

        heldJokers.add(counterJoker);
        heldJokers.add(gluttonousJoker);
        heldJokers.add(lustyJoker);
        heldJokers.add(wrathfulJoker);

        assertScoreEquals(heldJokers, 2212);
        heldJokers.add(greedyJoker);
        assertScoreEquals(heldJokers, 2686);
    }
}
