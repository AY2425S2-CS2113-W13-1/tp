package javatro.core;

/** Stores configuration details for a game round. */
public class RoundConfig {
    /** The minimum score required to win the round. */
    private final int blindScore;
    /** The name of the current round. */
    private String roundName;
    /** The description of the current round. */
    private String roundDescription;

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
}
