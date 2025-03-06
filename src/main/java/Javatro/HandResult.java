package Javatro;

import static Javatro.Card.Rank.ACE;
import static Javatro.Card.Rank.FIVE;
import static Javatro.Card.Rank.FOUR;
import static Javatro.Card.Rank.JACK;
import static Javatro.Card.Rank.KING;
import static Javatro.Card.Rank.QUEEN;
import static Javatro.Card.Rank.TEN;
import static Javatro.Card.Rank.THREE;
import static Javatro.Card.Rank.TWO;
import static Javatro.PokerHand.HandType.FLUSH;
import static Javatro.PokerHand.HandType.FOUR_OF_A_KIND;
import static Javatro.PokerHand.HandType.FULL_HOUSE;
import static Javatro.PokerHand.HandType.HIGH_CARD;
import static Javatro.PokerHand.HandType.PAIR;
import static Javatro.PokerHand.HandType.ROYAL_FLUSH;
import static Javatro.PokerHand.HandType.STRAIGHT;
import static Javatro.PokerHand.HandType.STRAIGHT_FLUSH;
import static Javatro.PokerHand.HandType.THREE_OF_A_KIND;
import static Javatro.PokerHand.HandType.TWO_PAIR;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The `HandResult` class evaluates a poker hand based on the given cards. It supports hands with 1
 * to 5 cards and determines the best possible poker hand.
 */
public class HandResult {

    /**
     * Evaluates the poker hand based on the given list of cards.
     *
     * @param cards A list of cards to evaluate. Must contain between 1 and 5 cards.
     * @return A `PokerHand` object representing the evaluated hand.
     * @throws IllegalArgumentException If the input is null or contains fewer than 1 card.
     */
    public static PokerHand evaluateHand(List<Card> cards) {

        if (cards == null || cards.isEmpty() || cards.size() > 5) {
            throw new IllegalArgumentException("A poker hand must contain between 1 and 5 cards.");
        }

        // Counts occurrences of each rank (e.g., {"A":1, "K":1, "Q":1, "J":1, "10":1} for a Royal
        // Flush).
        Map<Card.Rank, Integer> rankCount = new HashMap<>();
        // Counts occurrences of each suit.
        Map<Card.Suit, Integer> suitCount = new HashMap<>();

        // card.rank() retrieves the rank of the card (e.g., Rank.ACE, Rank.KING, Rank.TEN, etc.).
        // rankCount.getOrDefault(card.rank(), 0) checks if the rank is already in the map:
        //   - If yes, it gets the current count.
        //   - If not, it returns 0 (default value).
        // +1 increments the count for that rank.
        // put() updates the rank count in the map.
        for (Card card : cards) {
            rankCount.put(card.rank(), rankCount.getOrDefault(card.rank(), 0) + 1);
            suitCount.put(card.suit(), suitCount.getOrDefault(card.suit(), 0) + 1);
        }

        boolean isFlush = suitCount.containsValue(5); // All cards have the same suit
        boolean isStraight = isStraight(rankCount); // Cards form a consecutive sequence
        boolean isRoyal = isRoyal(rankCount); // Special case: A, K, Q, J, 10

        // Determine the best poker hand
        if (cards.size() == 1) {
            return new PokerHand(HIGH_CARD); // Single card is always a high card
        } else {
            return evaluateMultiCardHand(rankCount, isFlush, isStraight, isRoyal);
        }
    }

    /**
     * Evaluates poker hands with 2 to 5 cards using a priority-based approach.
     *
     * @param rankCount A map of card ranks and their counts.
     * @param isFlush Whether the hand is a flush.
     * @param isStraight Whether the hand is a straight.
     * @param isRoyal Whether the hand is a royal flush.
     * @return A `HandResult` object representing the evaluated hand.
     */
    private static PokerHand evaluateMultiCardHand(
            Map<Card.Rank, Integer> rankCount,
            boolean isFlush,
            boolean isStraight,
            boolean isRoyal) {

        // Check for the strongest hands first
        if (isRoyal && isFlush) {
            return new PokerHand(ROYAL_FLUSH);
        } else if (isStraight && isFlush) {
            return new PokerHand(STRAIGHT_FLUSH);
        } else if (rankCount.containsValue(4)) {
            return new PokerHand(FOUR_OF_A_KIND);
        } else if (rankCount.containsValue(3) && rankCount.containsValue(2)) {
            return new PokerHand(FULL_HOUSE);
        } else if (isFlush) {
            return new PokerHand(FLUSH);
        } else if (isStraight) {
            return new PokerHand(STRAIGHT);
        } else if (rankCount.containsValue(3)) {
            return new PokerHand(THREE_OF_A_KIND);
        } else if (rankCount.values().stream().filter(count -> count == 2).count() == 2) {
            return new PokerHand(TWO_PAIR);
        } else if (rankCount.containsValue(2)) {
            return new PokerHand(PAIR);
        } else {
            return new PokerHand(HIGH_CARD);
        }
    }

    /**
     * Checks if the given hand forms a straight (consecutive sequence of ranks).
     *
     * @param rankCount A map of card ranks and their counts.
     * @return `true` if the hand is a straight, `false` otherwise.
     */
    private static boolean isStraight(Map<Card.Rank, Integer> rankCount) {

        // rankCount.keySet() gets the unique card ranks in the hand.
        List<Card.Rank> ranks =
                rankCount.keySet().stream()
                        .sorted() // sorts the ranks in ascending order.
                        .toList(); // converts the result into a list.

        // A straight must have five distinct card ranks.
        if (ranks.size() == 5) {

            boolean normalStraight = true;

            // Loops through the sorted list and checks if each rank follows the previous rank
            // consecutively
            // ordinal() returns the numerical position of the enum value.
            // If the difference between consecutive ranks is not exactly 1, return false.
            for (int i = 0; i < 4; i++) {
                if (ranks.get(i + 1).ordinal() != ranks.get(i).ordinal() + 1) {
                    normalStraight = false;
                    break;
                }
            }

            // Special case: Ace-low straight (A-2-3-4-5)
            boolean aceLowStraight =
                    ranks.contains(ACE)
                            && ranks.contains(TWO)
                            && ranks.contains(THREE)
                            && ranks.contains(FOUR)
                            && ranks.contains(FIVE);

            return normalStraight || aceLowStraight;
        }

        return false;
    }

    /**
     * Checks if the given hand is a royal flush (A, K, Q, J, 10 of the same suit).
     *
     * @param rankCount A map of card ranks and their counts.
     * @return `true` if the hand is a royal flush, `false` otherwise.
     */
    private static boolean isRoyal(Map<Card.Rank, Integer> rankCount) {

        return rankCount.containsKey(ACE)
                && rankCount.containsKey(KING)
                && rankCount.containsKey(QUEEN)
                && rankCount.containsKey(JACK)
                && rankCount.containsKey(TEN);
    }
}
