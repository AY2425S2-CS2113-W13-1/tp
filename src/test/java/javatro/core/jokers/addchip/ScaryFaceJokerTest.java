package javatro.core.jokers.addchip;

import static javatro.core.Card.Rank.JACK;
import static javatro.core.Card.Rank.KING;
import static javatro.core.Card.Rank.NINE;
import static javatro.core.Card.Rank.QUEEN;
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

public class ScaryFaceJokerTest {
    private static List<Card> playedCardList;
    private static HeldJokers heldJokers;
    private static PokerHand result;
    private static Card cardOne;
    private static Card cardTwo;
    private static Card cardThree;
    private static Card cardFour;
    private static Card cardFive;
    private static Card cardSix;
    private static Card cardSeven;
    private static Joker scaryFaceJoker;

    /** Initialize a test run. */
    @BeforeEach
    void init() throws JavatroException {
        scaryFaceJoker = new ScaryFaceJoker();
        heldJokers = new HeldJokers();
        heldJokers.add(scaryFaceJoker);
        cardOne = new Card(NINE, DIAMONDS);
        cardTwo = new Card(NINE, DIAMONDS);
        cardThree = new Card(TEN, HEARTS);
        cardFour = new Card(TEN, HEARTS);
        cardFive = new Card(KING, SPADES);
        cardSix = new Card(QUEEN, SPADES);
        cardSeven = new Card(JACK, SPADES);
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
    void testHalfJokerCorrect() throws JavatroException {
        playedCardList = List.of(cardFive, cardSix, cardSeven);
        assertScoreEquals(heldJokers, 125);
    }

    /** Test that a hand without face cards does not trigger the ScaryFaceJoker. */
    @Test
    void testScaryFaceJokerDisabled() throws JavatroException {
        playedCardList = List.of(cardOne, cardTwo, cardThree, cardFour);
        assertScoreEquals(heldJokers, 116);
    }
}
