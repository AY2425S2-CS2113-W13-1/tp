package javatro.core;

/**
 * Represents the result of evaluating a poker hand with tracking for levels and play counts.
 *
 * @param handType The type of poker hand
 * @param level The current level of this hand (default 1)
 * @param playedCount How many times this hand has been played (default 0)
 */
public record PokerHand(HandType handType, int level, int playedCount) {

    // Default constructor for when level and playedCount aren't specified
    public PokerHand(HandType handType) {
        this(handType, 1, 0);
    }

    /**
     * Enum representing all possible poker hand types, along with their base chips and multipliers.
     */
    public enum HandType {
        FLUSH_FIVE("Flush Five", 160, 16),
        FLUSH_HOUSE("Flush House", 140, 14),
        FIVE_OF_A_KIND("Five of a Kind", 120, 12),
        ROYAL_FLUSH("Royal Flush", 100, 8),
        STRAIGHT_FLUSH("Straight Flush", 100, 8),
        FOUR_OF_A_KIND("Four of a Kind", 60, 7),
        FULL_HOUSE("Full House", 40, 4),
        FLUSH("Flush", 35, 4),
        STRAIGHT("Straight", 30, 4),
        THREE_OF_A_KIND("Three of a Kind", 30, 3),
        TWO_PAIR("Two Pair", 20, 2),
        PAIR("Pair", 10, 2),
        HIGH_CARD("High Card", 5, 1);

        private final String handName;
        private final int chips;
        private final int multiplier;

        HandType(String handName, int chips, int multiplier) {
            this.handName = handName;
            this.chips = chips;
            this.multiplier = multiplier;
        }

        public String getHandName() {
            return handName;
        }

        public int getChips() {
            return chips;
        }

        public int getMultiplier() {
            return multiplier;
        }
    }

    /**
     * Returns the base chips for this poker hand adjusted by level.
     *
     * @return The base chips multiplied by level
     */
    public int getChips() {
        return handType.getChips() * level;
    }

    /**
     * Returns the multiplier for this poker hand adjusted by level.
     *
     * @return The multiplier
     */
    public int getMultiplier() {
        return handType.getMultiplier();
    }

    /**
     * Returns the hand name for this poker hand.
     *
     * @return The hand name.
     */
    public String getHandName() {
        return handType.getHandName();
    }

    /**
     * Creates a new PokerHand with an incremented played count.
     *
     * @return A new PokerHand instance with playedCount + 1
     */
    public PokerHand incrementPlayed() {
        return new PokerHand(handType, level, playedCount + 1);
    }

    /**
     * Creates a new PokerHand with a specific played count.
     *
     * @param count The new played count
     * @return A new PokerHand instance with the specified playedCount
     */
    public PokerHand withPlayedCount(int count) {
        return new PokerHand(handType, level, count);
    }

    /**
     * Creates a new PokerHand with a specific level.
     *
     * @param newLevel The new level
     * @return A new PokerHand instance with the specified level
     */
    public PokerHand withLevel(int newLevel) {
        return new PokerHand(handType, newLevel, playedCount);
    }

    @Override
    public String toString() {
        return String.format(
                "%s (Level: %d, Chips: %d, Multiplier: %d, Played: %d)",
                handType.getHandName(), level, getChips(), handType.getMultiplier(), playedCount);
    }

    /**
     * Formats the hand for display in the PokerHandScreen.
     *
     * @return Formatted string for UI display
     */
    public String toDisplayString() {
        return String.format("%-10d%-15s%4d × %-10d# %d",
                level,
                handType.getHandName(),
                handType.getChips(),
                handType.getMultiplier(),
                playedCount);
    }
}