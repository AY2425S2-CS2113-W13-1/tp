package javatro_core;

import java.util.Objects;

/**
 * Represents a playing card with a rank and a suit. Each card has a rank (e.g., ACE, KING, QUEEN)
 * and a suit (e.g., HEARTS, SPADES). The rank also determines the chip value of the card.
 */
public record Card(Rank rank, Suit suit) {

    /** Enum representing the rank of a card, along with its symbol and chip value. */
    public enum Rank {
        TWO("2", 2),
        THREE("3", 3),
        FOUR("4", 4),
        FIVE("5", 5),
        SIX("6", 6),
        SEVEN("7", 7),
        EIGHT("8", 8),
        NINE("9", 9),
        TEN("10", 10),
        JACK("J", 10),
        QUEEN("Q", 10),
        KING("K", 10),
        ACE("A", 11);

        private final String symbol;
        private final int chips;

        /**
         * Constructs a rank with the given symbol and chip value.
         *
         * @param symbol The symbol representing the rank (e.g., "A" for ACE).
         * @param chips The chip value associated with the rank.
         */
        Rank(String symbol, int chips) {
            this.symbol = symbol;
            this.chips = chips;
        }

        /**
         * Returns the symbol of the rank.
         *
         * @return The symbol of the rank.
         */
        public String getSymbol() {
            return symbol;
        }

        /**
         * Returns the chip value of the rank.
         *
         * @return The chip value of the rank.
         */
        public int getChips() {
            return chips;
        }
    }

    /** Enum representing the suit of a card. */
    public enum Suit {
        HEARTS("Hearts"),
        CLUBS("Clubs"),
        SPADES("Spades"),
        DIAMONDS("Diamonds");

        private final String name;

        /**
         * Constructs a suit with the given name.
         *
         * @param name The name of the suit.
         */
        Suit(String name) {
            this.name = name;
        }

        /**
         * Returns the name of the suit.
         *
         * @return The name of the suit.
         */
        public String getName() {
            return name;
        }
    }

    /**
     * Returns the chip value of the card based on its rank.
     *
     * @return The chip value of the card.
     */
    public int getChips() {
        return rank.getChips();
    }

    @Override
    public String toString() {
        return String.format("%s of %s", rank.getSymbol(), suit.getName());
    }

    // For displaying the card in the game
    public String getSimplified() {
        String suitVal =
                Objects.equals(suit().getName(), "Hearts")
                        ? "H"
                        : Objects.equals(suit().getName(), "Clubs")
                                ? "C"
                                : Objects.equals(suit().getName(), "Spades")
                                        ? "S"
                                        : Objects.equals(suit().getName(), "Diamonds") ? "D" : "-";

        return rank.getSymbol() + suitVal;
    }
}
