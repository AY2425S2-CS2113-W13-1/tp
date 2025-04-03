package javatro.core;

import static javatro.core.Card.Rank.ACE;
import static javatro.core.Card.Rank.FIVE;
import static javatro.core.Card.Rank.FOUR;
import static javatro.core.Card.Rank.JACK;
import static javatro.core.Card.Rank.KING;
import static javatro.core.Card.Rank.QUEEN;
import static javatro.core.Card.Rank.TEN;
import static javatro.core.Card.Rank.THREE;
import static javatro.core.Card.Rank.TWO;
import static javatro.core.PokerHand.HandType.*;

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
    public static PokerHand evaluateHand(List<Card> cards) throws JavatroException {

        if (cards == null || cards.isEmpty() || cards.size() > 5) {
            throw JavatroException.invalidPlayedHand(1, 5);
        }

        // First check for Flush Five (all cards identical)
        if (isFlushFive(cards)) {
            return new PokerHand(FLUSH_FIVE);
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

        boolean isFlush = suitCount.containsValue(5); // All 5 cards have the same suit
        boolean isStraight = isStraight(rankCount); // Cards form a consecutive sequence
        boolean isRoyal = isRoyal(rankCount); // Special case: A, K, Q, J, 10

        // Check remaining special hands
        PokerHand specialHand = checkSpecialHands(rankCount, isFlush);
        if (specialHand != null) {
            return specialHand;
        }

        // Determine the best poker hand
        if (cards.size() == 1) {
            return new PokerHand(HIGH_CARD); // Single card is always a high card
        } else {
            return evaluateMultiCardHand(rankCount, isFlush, isStraight, isRoyal);
        }
    }

    /** Checks if all cards are exactly identical (same rank and suit) */
    private static boolean isFlushFive(List<Card> cards) {
        if (cards.size() != 5) return false;

        Card first = cards.get(0);
        for (Card card : cards) {
            if (!card.rank().equals(first.rank()) || !card.suit().equals(first.suit())) {
                return false;
            }
        }
        return true;
    }

    /** Checks for special poker hands (Flush Five, Flush House, Five of a Kind). */
    private static PokerHand checkSpecialHands(Map<Card.Rank, Integer> rankCount, boolean isFlush) {
        // Five of a Kind (all cards same rank, but possibly different suits)
        if (rankCount.size() == 1 && rankCount.values().iterator().next() == 5) {
            return new PokerHand(FIVE_OF_A_KIND);
        }

        // Flush House (full house where all cards same suit)
        if (isFlush && rankCount.size() == 2) {
            boolean hasThree = false;
            boolean hasTwo = false;
            for (int count : rankCount.values()) {
                if (count == 3) hasThree = true;
                if (count == 2) hasTwo = true;
            }
            if (hasThree && hasTwo) {
                return new PokerHand(FLUSH_HOUSE);
            }
        }

        return null;
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
        }
        // Default case: High Card
        return new PokerHand(HIGH_CARD);
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
