package javatro.core.round;

import javatro.core.BossType;

/** Stores configuration details for a game round. */
class RoundConfig {
    /** The initial number of cards dealt to the player. */
    public static final int INITIAL_HAND_SIZE = 8;
    /** The maximum number of discards allowed per round. */
    public static final int MAX_DISCARDS_PER_ROUND = 3;
    /** The maximum number of cards in a hand. */
    public static final int DEFAULT_MAX_HAND_SIZE = 5;
    /** The minimum number of cards in a hand. */
    public static final int DEFAULT_MIN_HAND_SIZE = 1;

    /** The minimum score required to win the round. */
    private final int blindScore;
    /** The name of the current round. */
    private String roundName;
    /** The description of the current round. */
    private String roundDescription;
    /** The maximum number of cards that can be played in this round. */
    private int maxHandSize = DEFAULT_MAX_HAND_SIZE;
    /** The minimum number of cards that can be played in this round. */
    private int minHandSize = DEFAULT_MIN_HAND_SIZE;
    /** The type of boss for this round. */
    private BossType bossType = BossType.NONE;

    /**
     * Creates a new round configuration.
     *
     * @param roundName The display name of the round
     * @param roundDescription The description of the round
     * @param blindScore The target score required to win
     */
    public RoundConfig(String roundName, String roundDescription, int blindScore) {
        this.roundName = roundName;
        this.roundDescription = roundDescription;
        this.blindScore = blindScore;
    }

    /**
     * Creates a new round configuration with default values.
     */
    public RoundConfig(int blindScore) {
        this("Default Round", "Default Description", blindScore);
    }

    /**
     * Gets the target score needed to win this round.
     *
     * @return The blind score target
     */
    public int getBlindScore() {
        return blindScore;
    }

    /**
     * Gets the display name of this round.
     *
     * @return The round name
     */
    public String getRoundName() {
        return roundName;
    }

    /**
     * Sets the display name of this round.
     *
     * @param roundName The new round name
     */
    public void setRoundName(String roundName) {
        this.roundName = roundName;
    }

    /**
     * Gets the description of this round.
     *
     * @return The round description
     */
    public String getRoundDescription() {
        return roundDescription;
    }

    /**
     * Sets the description of this round.
     *
     * @param roundDescription The new round description
     */
    public void setRoundDescription(String roundDescription) {
        this.roundDescription = roundDescription;
    }

    /**
     * Gets the maximum number of cards that can be played in this round.
     *
     * @return The maximum hand size
     */
    public int getMaxHandSize() {
        return maxHandSize;
    }

    /**
     * Sets the maximum number of cards that can be played in this round.
     *
     * @param maxHandSize The new maximum hand size
     */
    public void setMaxHandSize(int maxHandSize) {
        this.maxHandSize = maxHandSize;
    }

    /**
     * Gets the minimum number of cards that can be played in this round.
     *
     * @return The minimum hand size
     */
    public int getMinHandSize() {
        return minHandSize;
    }

    /**
     * Sets the minimum number of cards that can be played in this round.
     *
     * @param minHandSize The new minimum hand size
     */
    public void setMinHandSize(int minHandSize) {
        this.minHandSize = minHandSize;
    }

    /**
     * Gets the boss type for this round.
     *
     * @return The boss type
     */
    public BossType getBossType() {
        return bossType;
    }

    /**
     * Sets the boss type for this round.
     *
     * @param bossType The new boss type
     */
    public void setBossType(BossType bossType) {
        this.bossType = bossType;

        // Update round name and description to match boss type if it's not NONE
        if (bossType != BossType.NONE) {
            this.roundName = bossType.getName();
            this.roundDescription = bossType.getDescription();
        }
    }
}
