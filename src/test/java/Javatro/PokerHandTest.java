package Javatro;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javatro_core.Card;
import javatro_core.HandResult;
import javatro_core.PokerHand;

import org.junit.jupiter.api.Test;

import java.util.List;

class PokerHandTest {

    @Test
    void testRoyalFlush() {
        List<Card> hand =
                List.of(
                        new Card(Card.Rank.ACE, Card.Suit.HEARTS),
                        new Card(Card.Rank.KING, Card.Suit.HEARTS),
                        new Card(Card.Rank.QUEEN, Card.Suit.HEARTS),
                        new Card(Card.Rank.JACK, Card.Suit.HEARTS),
                        new Card(Card.Rank.TEN, Card.Suit.HEARTS));
        HandResult result = PokerHand.evaluateHand(hand);
        assertEquals("Royal Flush", result.handName());
    }

    @Test
    void testStraightFlush() {
        List<Card> hand =
                List.of(
                        new Card(Card.Rank.NINE, Card.Suit.SPADES),
                        new Card(Card.Rank.EIGHT, Card.Suit.SPADES),
                        new Card(Card.Rank.SEVEN, Card.Suit.SPADES),
                        new Card(Card.Rank.SIX, Card.Suit.SPADES),
                        new Card(Card.Rank.FIVE, Card.Suit.SPADES));
        HandResult result = PokerHand.evaluateHand(hand);
        assertEquals("Straight Flush", result.handName());
    }

    @Test
    void testFourOfAKind() {
        List<Card> hand =
                List.of(
                        new Card(Card.Rank.ACE, Card.Suit.CLUBS),
                        new Card(Card.Rank.ACE, Card.Suit.DIAMONDS),
                        new Card(Card.Rank.ACE, Card.Suit.HEARTS),
                        new Card(Card.Rank.ACE, Card.Suit.SPADES),
                        new Card(Card.Rank.TWO, Card.Suit.CLUBS));
        HandResult result = PokerHand.evaluateHand(hand);
        assertEquals("Four of a Kind", result.handName());
    }

    @Test
    void testFullHouse() {
        List<Card> hand =
                List.of(
                        new Card(Card.Rank.KING, Card.Suit.CLUBS),
                        new Card(Card.Rank.KING, Card.Suit.DIAMONDS),
                        new Card(Card.Rank.KING, Card.Suit.HEARTS),
                        new Card(Card.Rank.QUEEN, Card.Suit.SPADES),
                        new Card(Card.Rank.QUEEN, Card.Suit.CLUBS));
        HandResult result = PokerHand.evaluateHand(hand);
        assertEquals("Full House", result.handName());
    }

    @Test
    void testFlush() {
        List<Card> hand =
                List.of(
                        new Card(Card.Rank.TWO, Card.Suit.HEARTS),
                        new Card(Card.Rank.FOUR, Card.Suit.HEARTS),
                        new Card(Card.Rank.SIX, Card.Suit.HEARTS),
                        new Card(Card.Rank.EIGHT, Card.Suit.HEARTS),
                        new Card(Card.Rank.TEN, Card.Suit.HEARTS));
        HandResult result = PokerHand.evaluateHand(hand);
        assertEquals("Flush", result.handName());
    }

    @Test
    void testStraight() {
        List<Card> hand =
                List.of(
                        new Card(Card.Rank.SEVEN, Card.Suit.CLUBS),
                        new Card(Card.Rank.SIX, Card.Suit.DIAMONDS),
                        new Card(Card.Rank.FIVE, Card.Suit.HEARTS),
                        new Card(Card.Rank.FOUR, Card.Suit.SPADES),
                        new Card(Card.Rank.THREE, Card.Suit.CLUBS));
        HandResult result = PokerHand.evaluateHand(hand);
        assertEquals("Straight", result.handName());
    }

    @Test
    void testThreeOfAKind() {
        List<Card> hand =
                List.of(
                        new Card(Card.Rank.JACK, Card.Suit.CLUBS),
                        new Card(Card.Rank.JACK, Card.Suit.DIAMONDS),
                        new Card(Card.Rank.JACK, Card.Suit.HEARTS),
                        new Card(Card.Rank.SEVEN, Card.Suit.SPADES),
                        new Card(Card.Rank.FOUR, Card.Suit.CLUBS));
        HandResult result = PokerHand.evaluateHand(hand);
        assertEquals("Three of a Kind", result.handName());
    }

    @Test
    void testTwoPair() {
        List<Card> hand =
                List.of(
                        new Card(Card.Rank.TEN, Card.Suit.CLUBS),
                        new Card(Card.Rank.TEN, Card.Suit.DIAMONDS),
                        new Card(Card.Rank.FIVE, Card.Suit.HEARTS),
                        new Card(Card.Rank.FIVE, Card.Suit.SPADES),
                        new Card(Card.Rank.ACE, Card.Suit.CLUBS));
        HandResult result = PokerHand.evaluateHand(hand);
        assertEquals("Two Pair", result.handName());
    }

    @Test
    void testPair() {
        List<Card> hand =
                List.of(
                        new Card(Card.Rank.EIGHT, Card.Suit.CLUBS),
                        new Card(Card.Rank.EIGHT, Card.Suit.DIAMONDS),
                        new Card(Card.Rank.THREE, Card.Suit.HEARTS),
                        new Card(Card.Rank.SEVEN, Card.Suit.SPADES),
                        new Card(Card.Rank.FOUR, Card.Suit.CLUBS));
        HandResult result = PokerHand.evaluateHand(hand);
        assertEquals("Pair", result.handName());
    }

    @Test
    void testHighCard() {
        List<Card> hand =
                List.of(
                        new Card(Card.Rank.KING, Card.Suit.CLUBS),
                        new Card(Card.Rank.TEN, Card.Suit.DIAMONDS),
                        new Card(Card.Rank.SIX, Card.Suit.HEARTS),
                        new Card(Card.Rank.FOUR, Card.Suit.SPADES),
                        new Card(Card.Rank.TWO, Card.Suit.CLUBS));
        HandResult result = PokerHand.evaluateHand(hand);
        assertEquals("High Card", result.handName());
    }
}
