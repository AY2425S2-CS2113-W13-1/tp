package javatro.core;

// @author swethacool
/**
 * The {@code Ante} class represents the ante system in a poker game, managing the current round,
 * ante values, and blind progression.
 */
public class Ante {
    private Ante ante;
    private static final int MAX_ANTE_COUNT = 8;

    /** Enum representing different blind levels with associated multipliers and names. */
    public enum Blind {
        SMALL_BLIND(1.0, "SMALL BLIND"),
        LARGE_BLIND(1.5, "LARGE BLIND"),
        BOSS_BLIND(2.0, "BOSS BLIND");

        private final double multiplier;
        private final String name;

        /**
         * Constructs a Blind with a multiplier and a name.
         *
         * @param multiplier The multiplier applied to the ante score.
         * @param name The name of the blind level.
         */
        Blind(double multiplier, String name) {
            this.multiplier = multiplier;
            this.name = name;
        }

        /**
         * Gets the multiplier associated with this blind.
         *
         * @return The multiplier value.
         */
        public double getMultiplier() {
            return multiplier;
        }

        /**
         * Gets the name of the blind.
         *
         * @return The name of the blind.
         */
        public String getName() {
            return name;
        }
    }

    private static int anteCount;

    private Blind blind;
    private final int[] anteScore = {300, 800, 2000, 5000, 11000, 20000, 35000, 50000};

    /**
     * Constructs an {@code Ante} object with an initial ante count of 1 and the small blind level.
     */
    public Ante() {
        anteCount = 1;
        blind = Blind.SMALL_BLIND;
    }

    /**
     * Calculates the round score based on the ante level and blind multiplier.
     *
     * @return The round score as an integer.
     */
    public int getRoundScore() {
        return (int) (anteScore[anteCount - 1] * blind.multiplier);
    }

    /**
     * Gets the base ante score for the current round.
     *
     * @return The ante score as an integer.
     */
    public int getAnteScore() {
        return anteScore[anteCount - 1];
    }

    // @author K-J-Q
    /**
     * Sets the ante count for the game.
     *
     * @param anteCount The new ante count to set.
     * @throws IllegalArgumentException if the ante count is not between 1 and {@link
     *     #MAX_ANTE_COUNT}
     */
    public void setAnteCount(int anteCount) {
        if (anteCount < 1 || anteCount > MAX_ANTE_COUNT) {
            // not using javatro exception since this method should only be used in tests
            throw new IllegalArgumentException(
                    "Ante count must be between 1 and " + MAX_ANTE_COUNT);
        }
        Ante.anteCount = anteCount;
    }

    /** Resets the ante to the first round with the small blind. */
    public void resetAnte() {
        anteCount = 1;
    }

    /** Moves to the next round of the ante system, adjusting the blind level and ante count. */
    public void nextRound() {
        if (blind == Blind.SMALL_BLIND) {
            blind = Blind.LARGE_BLIND;
        } else if (blind == Blind.LARGE_BLIND) {
            blind = Blind.BOSS_BLIND;
        } else {
            if (anteCount == MAX_ANTE_COUNT) return;
            blind = Blind.SMALL_BLIND;
            anteCount++;
        }
    }

    /**
     * Sets the blind level manually.
     *
     * @param blind The new blind level.
     */
    public void setBlind(Blind blind) {
        this.blind = blind;
    }

    /**
     * Gets the current blind level.
     *
     * @return The current {@code Blind} level.
     */
    public Blind getBlind() {
        return blind;
    }

    /**
     * Gets the current ante count.
     *
     * @return The ante count as an integer.
     */
    public int getAnteCount() {
        return anteCount;
    }
}
