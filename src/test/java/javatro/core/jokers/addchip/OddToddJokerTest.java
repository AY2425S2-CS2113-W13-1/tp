package javatro.core.jokers.addchip;

import static javatro.core.Card.Rank.KING;
import static javatro.core.Card.Rank.NINE;
import static javatro.core.Card.Rank.TEN;
import static javatro.core.Card.Suit.DIAMONDS;
import static javatro.core.Card.Suit.HEARTS;
import static javatro.core.Card.Suit.SPADES;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javatro.core.Card;
import javatro.core.HandResult;
import javatro.core.JavatroException;
import javatro.core.PokerHand;
import javatro.core.Score;
import javatro.core.jokers.HeldJokers;
import javatro.core.jokers.Joker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class OddToddJokerTest {
    private static List<Card> playedCardList;
    private static HeldJokers heldJokers;
    private static PokerHand result;
    private static Card cardOne;
    private static Card cardTwo;
    private static Card cardThree;
    private static Card cardFour;
    private static Card cardFive;
    private static Joker oddToddJoker;

    /** Initialize a test run. */
    @BeforeEach
    void init() throws JavatroException {
        oddToddJoker = new OddToddJoker();
        heldJokers = new HeldJokers();
        heldJokers.add(oddToddJoker);
        cardOne = new Card(NINE, DIAMONDS);
        cardTwo = new Card(NINE, DIAMONDS);
        cardThree = new Card(TEN, HEARTS);
        cardFour = new Card(TEN, HEARTS);
        cardFive = new Card(KING, SPADES);
    }

    /** Test that a hand played triggers joker effects correctly and has the right score. */
    void assertScoreEquals(HeldJokers currentJokers, int expectedScore) throws JavatroException {
        result = HandResult.evaluateHand(playedCardList);
        Score scoreObject = new Score();
        long finalScore = scoreObject.getScore(result, playedCardList, currentJokers);
        assertEquals(expectedScore, finalScore);
    }

    /** Test that a hand with 3 cards played correctly triggers the ScaryFaceJoker. */
    @Test
    void testOddToddJokerCorrect() throws JavatroException {
        playedCardList = List.of(cardOne, cardTwo);
        assertScoreEquals(heldJokers, 180);
    }

    /** Test that a hand without face cards does not trigger the ScaryFaceJoker. */
    @Test
    void testOddToddJokerDisabled() throws JavatroException {
        playedCardList = List.of(cardThree, cardFour, cardFive);
        assertScoreEquals(heldJokers, 80);
    }
}
