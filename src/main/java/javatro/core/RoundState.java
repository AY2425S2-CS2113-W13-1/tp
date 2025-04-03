package javatro.core;

/**
 * Encapsulates the state of a round in the game, including score and play limits.
 *
 * @see Round The main game round class that uses this state
 */
public class RoundState {
    /** The player's current score in the round. */
    private long currentScore;
    /** The number of remaining discards allowed. */
    private int remainingDiscards;
    /** The number of remaining plays in the round. */
    private int remainingPlays;

    /**
     * Creates a new round state with initial values.
     *
     * @param currentScore The starting score for the round
     * @param remainingDiscards The initial number of discards available
     * @param remainingPlays The initial number of plays available
     */
    public RoundState(long currentScore, int remainingDiscards, int remainingPlays) {
        this.currentScore = currentScore;
        this.remainingDiscards = remainingDiscards;
        this.remainingPlays = remainingPlays;
    }

    /**
     * Gets the current score accumulated in this round.
     *
     * @return The current score
     */
    public long getCurrentScore() {
        return currentScore;
    }

    /**
     * Adds points to the current score.
     *
     * @param points The number of points to add to the score
     */
    public void addScore(long points) {
        currentScore += points;
    }

    /**
     * Gets the number of remaining discards available to the player.
     *
     * @return The number of remaining discards
     */
    public int getRemainingDiscards() {
        return remainingDiscards;
    }

    /**
     * Sets the number of remaining discards to a specified amount.
     *
     * @param amount The number of discards to set
     * @throws IllegalArgumentException if the amount is negative
     */
    public void setRemainingDiscards(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        remainingDiscards = amount;
    }

    /** Reduces the number of remaining discards by one. */
    public void decrementDiscards() {
        remainingDiscards--;
    }

    /**
     * Increases the number of remaining discards by a specified amount.
     *
     * @param amount The number of additional discards to add
     */
    public void increaseRemainingDiscards(int amount) {
        remainingDiscards += amount;
    }

    /**
     * Gets the number of remaining plays available to the player.
     *
     * @return The number of remaining plays
     */
    public int getRemainingPlays() {
        return remainingPlays;
    }

    /** Reduces the number of remaining plays by one. */
    public void decrementPlays() {
        remainingPlays--;
    }

    /**
     * Set the number of remaining plays by a specified amount.
     *
     * @param amount The number of additional plays to set
     * @throws IllegalArgumentException if the amount is negative
     */
    public void setRemainingPlays(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        remainingPlays = amount;
    }
}
