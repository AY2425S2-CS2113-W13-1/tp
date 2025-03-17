package javatro_core;

import javatro_exception.JavatroException;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

public class Round {
    public static final int INITIAL_HAND_SIZE = 8;
    private static final int POKER_HAND_SIZE = 5;
    public static final int MAX_DISCARDS_PER_ROUND = 3;

    private int currentScore;
    private int blindScore;
    private int remainingDiscards;
    private int remainingPlays;
    private Deck deck;
    private HoldingHand playerHand;
    private String roundName = "";
    private String roundDescription = "";

    private PropertyChangeSupport support = new PropertyChangeSupport(this); // Observable

    /**
     * Constructs a new round with the specified blind score. The blind score can be fetched from a
     * file or manually inputed.
     *
     * <p>//@param gameState The current state of the game.
     *
     * @throws JavatroException
     */
    public Round(
            int blindScore,
            int remainingPlays,
            Deck deck,
            String roundName,
            String roundDescription)
            throws JavatroException {
        this.currentScore = 0;
        this.blindScore = blindScore;
        this.remainingDiscards = MAX_DISCARDS_PER_ROUND;
        this.remainingPlays = remainingPlays;
        this.deck = deck;
        this.playerHand = new HoldingHand();

        // Default descriptions and names
        this.roundName = roundName;
        this.roundDescription = roundDescription;

        if (blindScore < 0) {
            throw JavatroException.invalidBlindScore();
        }

        if (remainingPlays <= 0) {
            throw JavatroException.invalidPlaysPerRound();
        }

        if (deck == null) {
            throw JavatroException.invalidDeck();
        }

        // Initial draw
        playerHand.draw(INITIAL_HAND_SIZE, this.deck);
    }

    /**
     * Registers an observer to listen for property changes.
     *
     * @param pcl The property change listener to register.
     */
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    /**
     * Fires property change events to notify observers of updated round variables. This method
     * updates UI components and other observers about the current game state.
     */
    public void updateRoundVariables() {
        support.firePropertyChange("blindScore", null, blindScore);
        support.firePropertyChange("remainingPlays", null, remainingPlays);
        support.firePropertyChange("remainingDiscards", null, remainingDiscards);
        support.firePropertyChange("roundName", null, roundName);
        support.firePropertyChange("roundDescription", null, roundDescription);
        support.firePropertyChange("holdingHand", null, getPlayerHand());
        support.firePropertyChange("currentScore", null, currentScore);

        if (isRoundOver() && isWon()) {
            support.firePropertyChange("roundComplete", null, 1);
        } else if (isRoundOver() && !isWon()) {
            support.firePropertyChange("roundComplete", null, -1);
        } else {
            support.firePropertyChange("roundComplete", null, 0);
        }
    }

    /**
     * Plays a set of 5 cards as a poker hand.
     *
     * @param cardIndices Indices of cards to play from the holding hand (must be exactly 5 cards)
     * @throws JavatroException
     * @throws IllegalArgumentException If the number of cards to play is not exactly 5
     */
    public void playCards(List<Integer> cardIndices) throws JavatroException {

        List<Card> playedCards = playerHand.play(cardIndices, this.deck);
        PokerHand result = HandResult.evaluateHand(playedCards);
        Integer totalChips = result.getChips();

        for (Card card : playedCards) {
            totalChips += card.getChips();
        }

        // Update round state
        currentScore += totalChips * result.getMultiplier();

        // Draw new cards to replace played ones
        playerHand.draw(POKER_HAND_SIZE, deck);

        remainingPlays--;

        updateRoundVariables();
    }

    /**
     * Discards cards from the player's hand.
     *
     * @param cardIndices Indices of cards to discard from the holding hand
     * @throws IllegalStateException If no remaining discards are available
     */
    public void discardCards(List<Integer> cardIndices) throws JavatroException {
        if (remainingDiscards <= 0) {
            throw new IllegalStateException("No remaining discards available");
        }
        playerHand.discard(cardIndices, deck);
        remainingDiscards--;

        playerHand.draw(cardIndices.size(), deck);

        updateRoundVariables();
    }

    // Getters
    public int getCurrentScore() {
        return currentScore;
    }

    public int getBlindScore() {
        return blindScore;
    }

    public int getRemainingDiscards() {
        return remainingDiscards;
    }

    public int getRemainingPlays() {
        return remainingPlays;
    }

    public List<Card> getPlayerHand() {
        return playerHand.getHand();
    }

    /**
     * Checks if the round is over based on game rules.
     *
     * @return true if the round is over, false otherwise
     */
    public boolean isRoundOver() {
        // Round ends if no plays are remaining
        return remainingPlays <= 0;
    }

    /**
     * Determines if the round was won.
     *
     * @return true if player won the round, false otherwise
     */
    public boolean isWon() {
        return currentScore >= blindScore;
    }

    public String getRoundName() {
        return roundName;
    }

    public void setRoundName(String roundName) {
        this.roundName = roundName;
    }

    public String getRoundDescription() {
        return roundDescription;
    }

    public void setRoundDescription(String roundDescription) {
        this.roundDescription = roundDescription;
    }
}
