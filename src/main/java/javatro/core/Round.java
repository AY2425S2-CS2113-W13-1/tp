package javatro.core;

import static javatro.core.Deck.DeckType;
import static javatro.core.Deck.DeckType.RED;
import static javatro.core.JavatroCore.heldJokers;

import javatro.core.jokers.HeldJokers;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Round {
    /** The initial number of cards dealt to the player. */
    public static final int INITIAL_HAND_SIZE = 8;
    /** The maximum number of discards allowed per round. */
    public static final int MAX_DISCARDS_PER_ROUND = 4;
    /** The number of cards required to form a valid poker hand. */
    private static final int POKER_HAND_SIZE = 5;

    /** The player's current hand of cards. */
    public HoldingHand playerHand;
    /** The player's current held jokers. */
    public HeldJokers playerJokers;
    /** The player's current score in the round. */
    private long currentScore;
    /** The minimum score required to win the round. */
    private final int blindScore;
    /** The number of remaining discards allowed. */
    private int remainingDiscards;
    /** The number of remaining plays in the round. */
    private int remainingPlays;

    /** The deck of cards used in the round. */
    public static Deck deck;

    /** The name of the current round. */
    private String roundName = "";
    /** The description of the current round. */
    private String roundDescription = "";

    /** Manages property change listeners for game state updates. */
    private final PropertyChangeSupport support = new PropertyChangeSupport(this); // Observable

    public PokerHand playedHand;
    public List<Card> selectedCards;

    /**
     * Constructs a new round with the specified ante and blind settings.
     *
     * @param remainingPlays The number of plays available.
     * @param deck The deck of cards used in the round.
     * @param roundName The name for the round.
     * @param roundDescription The description for the round.
     * @throws JavatroException If provided parameters are invalid.
     */
    public Round(
            int blindScore,
            int remainingPlays,
            Deck deck,
            HeldJokers heldJokers,
            String roundName,
            String roundDescription)
            throws JavatroException {
        this.currentScore = 0;

        this.remainingDiscards = MAX_DISCARDS_PER_ROUND;
        this.remainingPlays = remainingPlays;
        Round.deck = deck;
        this.playerHand = new HoldingHand();
        this.playerJokers = heldJokers;
        // Default descriptions and names
        this.roundName = roundName;
        this.roundDescription = roundDescription;
        this.blindScore = blindScore;

        if (blindScore < 0) {
            throw JavatroException.invalidBlindScore();
        }
        if (remainingPlays <= 0) {
            throw JavatroException.invalidPlaysPerRound();
        }
        if (deck == null) {
            throw JavatroException.invalidDeck();
        }

        // Handle special Deck variants here.
        DeckType deckName = deck.getDeckName();
        if (deckName == RED) {
            this.remainingDiscards += 1;
        } else if (deckName == DeckType.BLUE) {
            this.remainingPlays += 1;
        }

        // Initial draw
        playerHand.draw(INITIAL_HAND_SIZE, Round.deck);

        // Post-construction invariants
        assert this.currentScore == 0 : "Initial score must be zero";
        assert this.remainingDiscards == MAX_DISCARDS_PER_ROUND
                : "Initial discards must be set to maximum";
        assert this.playerHand.getHand().size() == INITIAL_HAND_SIZE
                : "Player should have exactly " + INITIAL_HAND_SIZE + " cards initially";
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
     * updates display components and other observers about the current game state.
     */
    public void updateRoundVariables() {
        support.firePropertyChange("blindScore", null, blindScore);
        support.firePropertyChange("remainingPlays", null, remainingPlays);
        support.firePropertyChange("remainingDiscards", null, remainingDiscards);
        support.firePropertyChange("roundName", null, roundName);
        support.firePropertyChange("roundDescription", null, roundDescription);
        support.firePropertyChange("holdingHand", null, getPlayerHand());
        support.firePropertyChange("currentScore", null, currentScore);
    }

    /**
     * Plays a set of cards as a poker hand.
     *
     * @param cardIndices Indices of cards to play from the holding hand
     */
    public void playCards(List<Integer> cardIndices) throws JavatroException {
        if (cardIndices.size() > POKER_HAND_SIZE || cardIndices.isEmpty()) {
            throw JavatroException.invalidPlayedHand();
        }

        if (remainingPlays <= 0) {
            throw JavatroException.noPlaysRemaining();
        }

        assert cardIndices != null : "Card indices cannot be null";
        assert !cardIndices.isEmpty() : "Card indices cannot be empty";
        assert cardIndices.size() <= POKER_HAND_SIZE
                : "Cannot play more than " + POKER_HAND_SIZE + " cards";
        assert remainingPlays > 0 : "No plays remaining to execute this action";

        long oldScore = currentScore;
        int oldRemainingPlays = remainingPlays;

        selectedCards = playerHand.play(cardIndices);
        playedHand = HandResult.evaluateHand(selectedCards);
        Score handScore = new Score();
        currentScore += handScore.getScore(playedHand, selectedCards, playerJokers);

        // Draw new cards to replace played ones
        playerHand.draw(cardIndices.size(), deck);

        remainingPlays--;

        // Post-condition assertions
        assert remainingPlays == oldRemainingPlays - 1
                : "Remaining plays should decrease by exactly 1";
        assert currentScore >= oldScore : "Score should not decrease after playing cards";
        assert playerHand.getHand().size() == INITIAL_HAND_SIZE
                : "Hand size should be maintained after play";

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
            throw JavatroException.noRemainingDiscards();
        }

        if (cardIndices.size() > POKER_HAND_SIZE) {
            throw JavatroException.tooManyDiscards();
        }

        if (cardIndices.size() < 1) {
            throw JavatroException.cannotDiscardZeroCards();
        }

        assert cardIndices != null : "Card indices cannot be null";
        assert !cardIndices.isEmpty() : "Cannot discard zero cards";
        assert remainingDiscards > 0 : "No discards remaining to execute this action";

        // Handle duplicates by using a Set
        Set<Integer> indicesToDiscard = new HashSet<>(cardIndices);
        int handSizeBefore = playerHand.getHand().size();
        int oldRemainingDiscards = remainingDiscards;

        selectedCards = playerHand.discard(cardIndices);
        remainingDiscards--;

        playerHand.draw(indicesToDiscard.size(), deck);

        // Post-condition assertions
        assert remainingDiscards == oldRemainingDiscards - 1
                : "Remaining discards should decrease by exactly 1";
        assert playerHand.getHand().size() == handSizeBefore
                : "Hand size should be maintained after discard";

        updateRoundVariables();
    }

    // Getters
    public long getCurrentScore() {
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
        assert playerHand != null : "Player hand cannot be null";
        return playerHand.getHand();
    }

    /**
     * Checks if the game is lost based on game rules.
     *
     * @return true if the game is lost, false otherwise
     */
    public boolean isLost() {
        // Game ends if no plays are remaining
        return remainingPlays <= 0 && !isWon();
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
