package Javatro;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PokerHand {
    // evaluateHand Method: This method takes an ArrayList of Card objects and evaluates the poker
    // hand based on the rules provided.
    public static HandResult evaluateHand(List<Card> cards) {
        if (cards == null || cards.size() != 5) {
            throw new IllegalArgumentException("A poker hand must contain exactly 5 cards.");
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

        // Poker Hands base chips and multiplier bonuses
        // Priority from Top to Bottom
        if (isRoyal && isFlush) {
            return new HandResult("Royal Flush", 100, 8);
        } else if (isStraight && isFlush) {
            return new HandResult("Straight Flush", 100, 8);
        } else if (rankCount.containsValue(4)) {
            return new HandResult("Four of a Kind", 60, 7);
        } else if (rankCount.containsValue(3) && rankCount.containsValue(2)) {
            return new HandResult("Full House", 40, 4);
        } else if (isFlush) {
            return new HandResult("Flush", 35, 4);
        } else if (isStraight) {
            return new HandResult("Straight", 30, 4);
        } else if (rankCount.containsValue(3)) {
            return new HandResult("Three of a Kind", 30, 3);
        } else if (rankCount.values().stream().filter(count -> count == 2).count() == 2) {
            return new HandResult("Two Pair", 20, 2);
        } else if (rankCount.containsValue(2)) {
            return new HandResult("Pair", 10, 2);
        } else {
            return new HandResult("High Card", 5, 1);
        }
    }

    // The isStraight method checks whether the given hand of five cards forms a consecutive
    // sequence
    private static boolean isStraight(Map<Card.Rank, Integer> rankCount) {
        // rankCount.keySet() gets the unique card ranks in the hand.
        List<Card.Rank> ranks =
                rankCount.keySet().stream()
                        .sorted() // sorts the ranks in ascending order.
                        .toList(); // converts the result into a list.

        // A straight must have five distinct card ranks.
        if (ranks.size() != 5) {
            return false;
        }

        // Loops through the sorted list and checks if each rank follows the previous rank
        // consecutively
        // ordinal() returns the numerical position of the enum value.
        // If the difference between consecutive ranks is not exactly 1, return false.
        for (int i = 0; i < ranks.size() - 1; i++) {
            if (ranks.get(i + 1).ordinal() != ranks.get(i).ordinal() + 1) {
                return false;
            }
        }

        // If all five cards are consecutive
        return true;
    }

    // isRoyal Method: Checks if the hand is a royal flush.
    private static boolean isRoyal(Map<Card.Rank, Integer> rankCount) {
        return rankCount.containsKey(Card.Rank.ACE)
                && rankCount.containsKey(Card.Rank.KING)
                && rankCount.containsKey(Card.Rank.QUEEN)
                && rankCount.containsKey(Card.Rank.JACK)
                && rankCount.containsKey(Card.Rank.TEN);
    }
}
