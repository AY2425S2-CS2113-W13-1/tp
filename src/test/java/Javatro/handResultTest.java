package Javatro;

import static Javatro.Card.Rank.ACE;
import static Javatro.Card.Rank.EIGHT;
import static Javatro.Card.Rank.FIVE;
import static Javatro.Card.Rank.FOUR;
import static Javatro.Card.Rank.JACK;
import static Javatro.Card.Rank.KING;
import static Javatro.Card.Rank.NINE;
import static Javatro.Card.Rank.QUEEN;
import static Javatro.Card.Rank.SEVEN;
import static Javatro.Card.Rank.SIX;
import static Javatro.Card.Rank.TEN;
import static Javatro.Card.Rank.THREE;
import static Javatro.Card.Rank.TWO;
import static Javatro.Card.Suit.CLUBS;
import static Javatro.Card.Suit.DIAMONDS;
import static Javatro.Card.Suit.HEARTS;
import static Javatro.Card.Suit.SPADES;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * This class contains unit tests for the {@link HandResult} class. It tests various poker hand
 * evaluations to ensure correct identification of hand types.
 */
class HandResultTest {

    /**
     * Tests that a straight flush is correctly identified. A straight flush is a hand that contains
     * five cards of sequential rank, all the same suit.
     */
    @Test
    void testStraightFlushTrue() throws JavatroException {
        List<Card> hand =
                List.of(
                        new Card(NINE, SPADES),
                        new Card(TEN, SPADES),
                        new Card(JACK, SPADES),
                        new Card(QUEEN, SPADES),
                        new Card(KING, SPADES));
        PokerHand result = HandResult.evaluateHand(hand);
        assertEquals("Straight Flush", result.getHandName());
    }

    /**
     * Tests that a hand with mixed suits is not identified as a straight flush. The hand should be
     * identified as a straight instead.
     */
    @Test
    void testStraightFlushFalseMixedSuits() throws JavatroException {
        List<Card> hand =
                List.of(
                        new Card(NINE, SPADES),
                        new Card(TEN, SPADES),
                        new Card(JACK, SPADES),
                        new Card(QUEEN, SPADES),
                        new Card(KING, DIAMONDS));
        PokerHand result = HandResult.evaluateHand(hand);
        assertNotEquals("Straight Flush", result.getHandName());
        assertEquals("Straight", result.getHandName());
    }

    /**
     * Tests that a hand with cards not in sequence is not identified as a straight flush. The hand
     * should be identified as a flush instead.
     */
    @Test
    void testStraightFlushFalseNotInSequence() throws JavatroException {
        List<Card> hand =
                List.of(
                        new Card(NINE, SPADES),
                        new Card(TEN, SPADES),
                        new Card(JACK, SPADES),
                        new Card(QUEEN, SPADES),
                        new Card(ACE, SPADES));
        PokerHand result = HandResult.evaluateHand(hand);
        assertNotEquals("Straight Flush", result.getHandName());
        assertEquals("Flush", result.getHandName());
    }

    /**
     * Tests that a royal flush is correctly identified. A royal flush is a hand that contains the
     * Ace, King, Queen, Jack, and Ten of the same suit.
     */
    @Test
    void testRoyalFlushTrue() throws JavatroException {
        List<Card> hand =
                List.of(
                        new Card(TEN, SPADES),
                        new Card(JACK, SPADES),
                        new Card(QUEEN, SPADES),
                        new Card(KING, SPADES),
                        new Card(ACE, SPADES));
        PokerHand result = HandResult.evaluateHand(hand);
        assertEquals("Royal Flush", result.getHandName());
    }

    /**
     * Tests that a hand with an Ace as a low card is not identified as a royal flush. The hand
     * should be identified as a straight flush instead.
     */
    @Test
    void testRoyalFlushFalseAceLow() throws JavatroException {
        List<Card> hand =
                List.of(
                        new Card(ACE, SPADES),
                        new Card(TWO, SPADES),
                        new Card(THREE, SPADES),
                        new Card(FOUR, SPADES),
                        new Card(FIVE, SPADES));
        PokerHand result = HandResult.evaluateHand(hand);
        assertNotEquals("Royal Flush", result.getHandName());
        assertEquals("Straight Flush", result.getHandName());
    }

    /**
     * Tests that a flush is correctly identified. A flush is a hand that contains five cards of the
     * same suit, not in sequence.
     */
    @Test
    void testFlushTrue() throws JavatroException {
        List<Card> hand =
                List.of(
                        new Card(FIVE, SPADES),
                        new Card(SIX, SPADES),
                        new Card(ACE, SPADES),
                        new Card(TWO, SPADES),
                        new Card(NINE, SPADES));
        PokerHand result = HandResult.evaluateHand(hand);
        assertEquals("Flush", result.getHandName());
    }

    /**
     * Tests that a hand with mixed suits is not identified as a flush. The hand should be
     * identified as a high card instead.
     */
    @Test
    void testFlushFalseMixedSuits() throws JavatroException {
        List<Card> hand =
                List.of(
                        new Card(FIVE, SPADES),
                        new Card(SIX, DIAMONDS),
                        new Card(ACE, SPADES),
                        new Card(TWO, SPADES),
                        new Card(NINE, SPADES));
        PokerHand result = HandResult.evaluateHand(hand);
        assertNotEquals("Flush", result.getHandName());
        assertEquals("High Card", result.getHandName());
    }

    /**
     * Tests that a hand with less than five cards is not identified as a flush. The hand should be
     * identified as a high card instead.
     */
    @Test
    void testFlushFalseLessThan5Cards() throws JavatroException {
        List<Card> hand =
                List.of(
                        new Card(FIVE, SPADES),
                        new Card(SIX, SPADES),
                        new Card(ACE, SPADES),
                        new Card(TWO, SPADES));
        PokerHand result = HandResult.evaluateHand(hand);
        assertNotEquals("Flush", result.getHandName());
        assertEquals("High Card", result.getHandName());
    }

    /**
     * Tests that a high card is correctly identified. A high card is a hand that does not fall into
     * any other category.
     */
    @Test
    void testHighCardTrue() throws JavatroException {
        List<Card> hand = List.of(new Card(ACE, SPADES));
        PokerHand result = HandResult.evaluateHand(hand);
        assertEquals("High Card", result.getHandName());
    }

    /**
     * Tests that a straight is correctly identified. A straight is a hand that contains five cards
     * of sequential rank, not all the same suit.
     */
    @Test
    void testStraightTrue() throws JavatroException {
        List<Card> hand =
                List.of(
                        new Card(FIVE, SPADES),
                        new Card(SIX, SPADES),
                        new Card(SEVEN, CLUBS),
                        new Card(EIGHT, SPADES),
                        new Card(NINE, DIAMONDS));
        PokerHand result = HandResult.evaluateHand(hand);
        assertEquals("Straight", result.getHandName());
    }

    /**
     * Tests that a hand with cards not in sequence is not identified as a straight. The hand should
     * be identified as a high card instead.
     */
    @Test
    void testStraightFalseNotInSequence() throws JavatroException {
        List<Card> hand =
                List.of(
                        new Card(FIVE, SPADES),
                        new Card(SIX, SPADES),
                        new Card(SEVEN, SPADES),
                        new Card(EIGHT, SPADES),
                        new Card(JACK, DIAMONDS));
        PokerHand result = HandResult.evaluateHand(hand);
        assertNotEquals("Straight", result.getHandName());
        assertEquals("High Card", result.getHandName());
    }

    /**
     * Tests that a hand with mixed sequence is not identified as a straight. The hand should be
     * identified as a high card instead.
     */
    @Test
    void testStraightFalseMixedSequence() throws JavatroException {
        List<Card> hand =
                List.of(
                        new Card(FIVE, SPADES),
                        new Card(SIX, SPADES),
                        new Card(EIGHT, SPADES),
                        new Card(NINE, SPADES),
                        new Card(TEN, DIAMONDS));
        PokerHand result = HandResult.evaluateHand(hand);
        assertNotEquals("Straight", result.getHandName());
        assertEquals("High Card", result.getHandName());
    }

    /**
     * Tests that a hand with less than five cards is not identified as a straight. The hand should
     * be identified as a high card instead.
     */
    @Test
    void testStraightFalseLessThan5Cards() throws JavatroException {
        List<Card> hand =
                List.of(
                        new Card(FIVE, SPADES),
                        new Card(SIX, SPADES),
                        new Card(SEVEN, SPADES),
                        new Card(EIGHT, SPADES));
        PokerHand result = HandResult.evaluateHand(hand);
        assertNotEquals("Straight", result.getHandName());
        assertEquals("High Card", result.getHandName());
    }

    /**
     * Tests that a full house is correctly identified. A full house is a hand that contains three
     * cards of one rank and two cards of another rank.
     */
    @Test
    void testFullHouseTrue() throws JavatroException {
        List<Card> hand =
                List.of(
                        new Card(FIVE, SPADES),
                        new Card(FIVE, DIAMONDS),
                        new Card(FIVE, CLUBS),
                        new Card(JACK, SPADES),
                        new Card(JACK, HEARTS));
        PokerHand result = HandResult.evaluateHand(hand);
        assertEquals("Full House", result.getHandName());
    }

    /**
     * Tests that a hand with two pairs is not identified as a full house. The hand should be
     * identified as two pairs instead.
     */
    @Test
    void testFullHouseFalseTwoPair() throws JavatroException {
        List<Card> hand =
                List.of(
                        new Card(FIVE, SPADES),
                        new Card(FIVE, DIAMONDS),
                        new Card(ACE, CLUBS),
                        new Card(JACK, SPADES),
                        new Card(JACK, HEARTS));
        PokerHand result = HandResult.evaluateHand(hand);
        assertNotEquals("Full House", result.getHandName());
        assertEquals("Two Pair", result.getHandName());
    }

    /**
     * Tests that a hand with less than five cards is not identified as a full house. The hand
     * should be identified as two pairs instead.
     */
    @Test
    void testFullHouseFalseLessThan5Cards() throws JavatroException {
        List<Card> hand =
                List.of(
                        new Card(FIVE, SPADES),
                        new Card(FIVE, DIAMONDS),
                        new Card(JACK, SPADES),
                        new Card(JACK, HEARTS));
        PokerHand result = HandResult.evaluateHand(hand);
        assertNotEquals("Full House", result.getHandName());
        assertEquals("Two Pair", result.getHandName());
    }

    /**
     * Tests that a hand with three of a kind is not identified as a full house. The hand should be
     * identified as three of a kind instead.
     */
    @Test
    void testFullHouseFalseThreeOfAKind() throws JavatroException {
        List<Card> hand =
                List.of(
                        new Card(FIVE, SPADES),
                        new Card(FIVE, DIAMONDS),
                        new Card(FIVE, HEARTS),
                        new Card(JACK, SPADES));
        PokerHand result = HandResult.evaluateHand(hand);
        assertNotEquals("Full House", result.getHandName());
        assertEquals("Three of a Kind", result.getHandName());
    }

    /**
     * Tests that three of a kind is correctly identified. Three of a kind is a hand that contains
     * three cards of one rank and two cards of two different ranks.
     */
    @Test
    void testThreeOfAKindTrue() throws JavatroException {
        List<Card> hand =
                List.of(
                        new Card(FIVE, SPADES),
                        new Card(FIVE, DIAMONDS),
                        new Card(FIVE, HEARTS),
                        new Card(JACK, SPADES),
                        new Card(ACE, CLUBS));
        PokerHand result = HandResult.evaluateHand(hand);
        assertEquals("Three of a Kind", result.getHandName());
    }

    /** Tests that three of a kind is correctly identified with only three cards. */
    @Test
    void testThreeOfAKindTrue3Cards() throws JavatroException {
        List<Card> hand =
                List.of(new Card(TEN, SPADES), new Card(TEN, DIAMONDS), new Card(TEN, HEARTS));
        PokerHand result = HandResult.evaluateHand(hand);
        assertEquals("Three of a Kind", result.getHandName());
    }

    /**
     * Tests that a full house is not identified as three of a kind. The hand should be identified
     * as a full house instead.
     */
    @Test
    void testThreeOfAKindFalseFullHouse() throws JavatroException {
        List<Card> hand =
                List.of(
                        new Card(FIVE, SPADES),
                        new Card(FIVE, DIAMONDS),
                        new Card(FIVE, CLUBS),
                        new Card(JACK, SPADES),
                        new Card(JACK, HEARTS));
        PokerHand result = HandResult.evaluateHand(hand);
        assertNotEquals("Three of a Kind", result.getHandName());
        assertEquals("Full House", result.getHandName());
    }

    /**
     * Tests that four of a kind is correctly identified with five cards. Four of a kind is a hand
     * that contains four cards of one rank and one card of another rank.
     */
    @Test
    void testFourOfAKindTrue5Cards() throws JavatroException {
        List<Card> hand =
                List.of(
                        new Card(EIGHT, SPADES),
                        new Card(EIGHT, DIAMONDS),
                        new Card(EIGHT, CLUBS),
                        new Card(EIGHT, HEARTS),
                        new Card(JACK, HEARTS));
        PokerHand result = HandResult.evaluateHand(hand);
        assertEquals("Four of a Kind", result.getHandName());
    }

    /** Tests that four of a kind is correctly identified with four cards. */
    @Test
    void testFourOfAKindTrue4Cards() throws JavatroException {
        List<Card> hand =
                List.of(
                        new Card(EIGHT, SPADES),
                        new Card(EIGHT, DIAMONDS),
                        new Card(EIGHT, CLUBS),
                        new Card(EIGHT, HEARTS));
        PokerHand result = HandResult.evaluateHand(hand);
        assertEquals("Four of a Kind", result.getHandName());
    }

    /**
     * Tests that a pair is correctly identified with five cards. A pair is a hand that contains two
     * cards of one rank and three cards of three different ranks.
     */
    @Test
    void testPairTrue5Cards() throws JavatroException {
        List<Card> hand =
                List.of(
                        new Card(EIGHT, SPADES),
                        new Card(EIGHT, DIAMONDS),
                        new Card(FIVE, CLUBS),
                        new Card(ACE, SPADES),
                        new Card(SEVEN, SPADES));
        PokerHand result = HandResult.evaluateHand(hand);
        assertEquals("Pair", result.getHandName());
    }

    /**
     * Tests that a hand with two pairs is not identified as a single pair. The hand should be
     * identified as two pairs instead.
     */
    @Test
    void testPairFalseTwoPair() throws JavatroException {
        List<Card> hand =
                List.of(
                        new Card(EIGHT, SPADES),
                        new Card(EIGHT, DIAMONDS),
                        new Card(ACE, CLUBS),
                        new Card(ACE, SPADES),
                        new Card(SEVEN, SPADES));
        PokerHand result = HandResult.evaluateHand(hand);
        assertNotEquals("Pair", result.getHandName());
        assertEquals("Two Pair", result.getHandName());
    }

    /** Tests that a pair is correctly identified with two cards. */
    @Test
    void testPairTrue2Cards() throws JavatroException {
        List<Card> hand = List.of(new Card(EIGHT, SPADES), new Card(EIGHT, DIAMONDS));
        PokerHand result = HandResult.evaluateHand(hand);
        assertEquals("Pair", result.getHandName());
    }

    /**
     * Tests that a full house is not identified as a pair. The hand should be identified as a full
     * house instead.
     */
    @Test
    void testPairFalseFullHouse() throws JavatroException {
        List<Card> hand =
                List.of(
                        new Card(FIVE, SPADES),
                        new Card(FIVE, DIAMONDS),
                        new Card(FIVE, CLUBS),
                        new Card(JACK, SPADES),
                        new Card(JACK, HEARTS));
        PokerHand result = HandResult.evaluateHand(hand);
        assertNotEquals("Pair", result.getHandName());
        assertEquals("Full House", result.getHandName());
    }

    /**
     * Tests that four of a kind is not identified as a pair. The hand should be identified as four
     * of a kind instead.
     */
    @Test
    void testPairFalseFourOfAKind() throws JavatroException {
        List<Card> hand =
                List.of(
                        new Card(EIGHT, SPADES),
                        new Card(EIGHT, DIAMONDS),
                        new Card(EIGHT, CLUBS),
                        new Card(EIGHT, HEARTS),
                        new Card(JACK, HEARTS));
        PokerHand result = HandResult.evaluateHand(hand);
        assertNotEquals("Pair", result.getHandName());
        assertEquals("Four of a Kind", result.getHandName());
    }

    /**
     * Tests that two pairs are correctly identified. Two pairs is a hand that contains two cards of
     * one rank, two cards of another rank, and one card of a third rank.
     */
    @Test
    void testTwoPairTrue() throws JavatroException {
        List<Card> hand =
                List.of(
                        new Card(EIGHT, SPADES),
                        new Card(EIGHT, DIAMONDS),
                        new Card(ACE, CLUBS),
                        new Card(ACE, SPADES),
                        new Card(SEVEN, SPADES));
        PokerHand result = HandResult.evaluateHand(hand);
        assertEquals("Two Pair", result.getHandName());
    }

    /**
     * Tests that a full house is not identified as two pairs. The hand should be identified as a
     * full house instead.
     */
    @Test
    void testTwoPairFalseFullHouse() throws JavatroException {
        List<Card> hand =
                List.of(
                        new Card(FIVE, SPADES),
                        new Card(FIVE, DIAMONDS),
                        new Card(FIVE, CLUBS),
                        new Card(JACK, SPADES),
                        new Card(JACK, HEARTS));
        PokerHand result = HandResult.evaluateHand(hand);
        assertNotEquals("Two Pair", result.getHandName());
        assertEquals("Full House", result.getHandName());
    }

    /**
     * Tests that four of a kind is not identified as two pairs. The hand should be identified as
     * four of a kind instead.
     */
    @Test
    void testTwoPairFalseFourOfAKind() throws JavatroException {
        List<Card> hand =
                List.of(
                        new Card(FIVE, SPADES),
                        new Card(FIVE, DIAMONDS),
                        new Card(FIVE, CLUBS),
                        new Card(FIVE, HEARTS),
                        new Card(JACK, HEARTS));
        PokerHand result = HandResult.evaluateHand(hand);
        assertNotEquals("Two Pair", result.getHandName());
        assertEquals("Four of a Kind", result.getHandName());
    }
}
