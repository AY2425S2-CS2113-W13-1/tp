package javatro.core;

/**
 * Represents the result of evaluating a poker hand with tracking for levels and play counts.
 *
 * @param handType The type of poker hand
 */
public record PokerHand(HandType handType) {

    /**
     * Returns the base chips for this poker hand adjusted by level.
     *
     * @return The base chips multiplied by level
     */
    public int getChips() {
        int base = handType.getChips();
        int level = PlanetCard.getLevel(handType);
        int chipIncrement = PlanetCard.getChipIncrement(handType);
        return base + (level - 1) * chipIncrement;
    }

    /**
     * Returns the multiplier for this poker hand adjusted by level.
     *
     * @return The multiplier
     */
    public int getMultiplier() {
        int base = handType.getMultiplier();
        int level = PlanetCard.getLevel(handType);
        int multiIncrement = PlanetCard.getMultiIncrement(handType);
        return base + (level - 1) * multiIncrement;
    }

    /**
     * Returns the hand name for this poker hand.
     *
     * @return The hand name.
     */
    public String getHandName() {
        return handType.getHandName();
    }

    public int getPlayCount() {
        return JavatroCore.getPlayCount(handType);
    }

    /**
     * Creates a new PokerHand with an incremented played count.
     *
     * @return A new PokerHand instance with playCount + 1
     */
    public PokerHand incrementPlayed() {
        JavatroCore.incrementPlayCount(handType);
        return this; // Return same instance since stats are managed by JavatroCore
    }

    @Override
    public String toString() {
        return String.format(
                "%s (Level: %d, Chips: %d, Multiplier: %d, Played: %d)",
                handType.getHandName(),
                PlanetCard.getLevel(handType),
                getChips(),
                handType.getMultiplier(),
                getPlayCount());
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
}
