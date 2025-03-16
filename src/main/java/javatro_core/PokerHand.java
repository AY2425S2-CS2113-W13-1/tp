package javatro_core;

/**
 * Represents the result of evaluating a poker hand.
 *
 * @param handType The type of poker hand.
 */
public record PokerHand(HandType handType) {

    /**
     * Enum representing all possible poker hand types, along with their base chips and multipliers.
     */
    public enum HandType {
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
     * Returns the base chips for this poker hand.
     *
     * @return The base chips.
     */
    public int getChips() {
        return handType.getChips();
    }

    /**
     * Returns the multiplier for this poker hand.
     *
     * @return The multiplier.
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

    @Override
    public String toString() {
        return String.format(
                "%s (Base Chips: %d, Multiplier: %d)",
                handType.getHandName(), handType.getChips(), handType.getMultiplier());
    }
}
