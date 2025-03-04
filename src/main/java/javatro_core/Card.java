package javatro_core;

public record Card(Rank rank, Suit suit) {
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

        Rank(String symbol, int chips) {
            this.symbol = symbol;
            this.chips = chips;
        }

        public String getSymbol() {
            return symbol;
        }

        public int getChips() {
            return chips;
        }
    }

    public enum Suit {
        HEARTS("Hearts"),
        CLUBS("Clubs"),
        SPADES("Spades"),
        DIAMONDS("Diamonds");

        private final String name;

        Suit(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    // Custom method to get the chip value of the card
    public int getChips() {
        return rank.getChips();
    }

    @Override
    public String toString() {
        return String.format("%s of %s", rank.getSymbol(), suit.getName());
    }
}
