package javatro.core.round;

import javatro.core.Ante;
import javatro.core.BossType;
import javatro.core.Card;
import javatro.core.Deck;
import javatro.core.Deck.DeckType;
import javatro.core.HandResult;
import javatro.core.HoldingHand;
import javatro.core.JavatroException;
import javatro.core.PokerHand;
import javatro.core.jokers.HeldJokers;
import javatro.core.round.RoundActions.ActionResult;

import java.beans.PropertyChangeListener;
import java.util.List;

/** Represents a round in the Javatro game. */
public class Round {

    /** The state of the current round. */
    private final RoundState state;
    /** The configuration of the current round. */
    private final RoundConfig config;
    /** The observer pattern implementation. */
    private final RoundObservable observable;

    /**
     * Constructs a new round with detailed configuration.
     *
     * @param ante The ante configuration containing the target score for this round
     * @param remainingPlays The initial number of plays allowed in this round
     * @param deck The deck of cards to be used for this round
     * @param heldJokers The player's collection of jokers available for this round
     * @param roundName The display name for this round
     * @param roundDescription A detailed description of the round's rules or theme
     * @throws JavatroException If any of the provided parameters are invalid
     */
    public Round(
            Ante ante,
            int remainingPlays,
            Deck deck,
            HeldJokers heldJokers,
            String roundName,
            String roundDescription)
            throws JavatroException {

        validateParameters(ante, remainingPlays, deck);

        // Create config with round name and description
        this.config = new RoundConfig(roundName, roundDescription, ante.getRoundScore());

        // Create state with initial values
        this.state =
                new RoundState(
                        0, RoundConfig.MAX_DISCARDS_PER_ROUND, remainingPlays, heldJokers, deck);

        // Apply special deck modifications
        applyDeckVariants(deck);

        // Apply special rules based on ante configuration
        applyAnteInvariants(ante);

        // Apply boss type
        applyBossVariants();

        // Create observable for notifications
        this.observable = new RoundObservable(this);

        validatePostConstruction();
    }

    /**
     * Constructs a new round with specified boss type
     *
     * @warning This constructor is not intended for use in normal gameplay. It is only for testing
     *     purposes.
     * @param ante The ante configuration to use
     * @param remainingPlays The number of plays allowed in this round
     * @param deck The deck of cards to be used for this round
     * @param heldJokers The player's collection of jokers available for this round
     * @throws JavatroException If any of the provided parameters are invalid
     */
    public Round(Ante ante, int remainingPlays, Deck deck, HeldJokers heldJokers, BossType bossType)
            throws JavatroException {
        this(ante, remainingPlays, deck, heldJokers, "Default Round", "Default Description");
        resetVarients();
        applyDeckVariants(deck);
        applyAnteInvariants(ante);
        setBossType(bossType);
    }

    /**
     * Constructs a new round with the specified ante and blind settings without specifying round
     * name and description.
     */
    public Round(Ante ante, int remainingPlays, Deck deck, HeldJokers heldJokers)
            throws JavatroException {
        this(ante, remainingPlays, deck, heldJokers, "Default Round", "Default Description");
    }

    /** Resets the boss type and deck variants to their default values. */
    private void resetVarients() {
        this.config.setBossType(BossType.NONE);
        this.state.setRemainingDiscards(RoundConfig.MAX_DISCARDS_PER_ROUND);
        this.state.setRemainingPlays(3);

        this.config.setMinHandSize(RoundConfig.DEFAULT_MIN_HAND_SIZE);
        this.config.setMaxHandSize(RoundConfig.DEFAULT_MAX_HAND_SIZE);
    }

    /**
     * Validates the input parameters for round creation.
     *
     * @param ante The ante configuration to validate
     * @param remainingPlays The number of plays to validate
     * @param deck The deck to validate
     * @throws JavatroException If validation fails for any parameter
     */
    private void validateParameters(Ante ante, int remainingPlays, Deck deck)
            throws JavatroException {
        if (ante.getRoundScore() < 0) {
            throw JavatroException.invalidBlindScore();
        }
        if (remainingPlays <= 0) {
            throw JavatroException.invalidPlaysPerRound();
        }
        if (deck == null) {
            throw JavatroException.invalidDeck();
        }
    }

    /**
     * Applies special rules based on the deck variant.
     *
     * @param deck The deck to check for special variants
     */
    private void applyDeckVariants(Deck deck) {
        DeckType deckName = deck.getDeckName();
        if (deckName == DeckType.RED) {
            this.state.increaseRemainingDiscards(1);
        } else if (deckName == DeckType.BLUE) {
            this.state.setRemainingPlays(this.state.getRemainingPlays() + 1);
        }
    }

    /**
     * Applies special rules based on the ante configuration.
     *
     * @param ante The ante configuration to check for special rules
     */
    private void applyAnteInvariants(Ante ante) {
        if (ante.getBlind() == Ante.Blind.BOSS_BLIND) {
            BossType randomBoss = BossType.getRandomBossType();
            this.config.setBossType(randomBoss);
        }
    }

    /** Applies special rules based on the selected boss type. */
    private void applyBossVariants() {
        BossType bossType = this.config.getBossType();
        switch (bossType) {
         }
    }

    /** Validates the post-construction state of the round. */
    private void validatePostConstruction() {
        assert this.state.getCurrentScore() == 0 : "Initial score must be zero";
        assert this.state.getPlayerHand().getHand().size() == RoundConfig.INITIAL_HAND_SIZE
                : "Player should have exactly "
                        + RoundConfig.INITIAL_HAND_SIZE
                        + " cards initially";
    }

    /**
     * Registers a listener for round state changes.
     *
     * @param pcl The property change listener to register
     */
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        observable.addPropertyChangeListener(pcl);
    }

    /** Notifies all registered observers of changes in the round state. */
    public void updateRoundVariables() {
        observable.updateRoundVariables(state, config);
    }

    /**
     * Plays a selection of cards from the player's hand.
     *
     * @param cardIndices Indices of cards to play from the holding hand
     * @throws JavatroException If the play is invalid or no plays remain
     */
    public void playCards(List<Integer> cardIndices) throws JavatroException {
        ActionResult result = RoundActions.playCards(state, config, cardIndices);

        state.setChosenCards(result.getCards());
        state.addScore(result.getPointsEarned());
        state.decrementPlays();

        observable.updateRoundVariables(state, config);
    }

    /**
     * Discards a selection of cards from the player's hand.
     *
     * @param cardIndices Indices of cards to discard from the holding hand
     * @throws JavatroException If the discard is invalid or no discards remain
     */
    public void discardCards(List<Integer> cardIndices) throws JavatroException {
        ActionResult result = RoundActions.discardCards(state, config, cardIndices);

        state.setChosenCards(result.getCards());
        state.decrementDiscards();

        observable.updateRoundVariables(state, config);
    }

    /**
     * Gets the current score accumulated in this round.
     *
     * @return The current score
     */
    public long getCurrentScore() {
        return state.getCurrentScore();
    }

    public void setCurrentScore(int score) {
        state.addScore(-1 * state.getCurrentScore());
        state.addScore(score);
    }

    /**
     * Gets the target score needed to win this round.
     *
     * @return The blind score target
     */
    public int getBlindScore() {
        return config.getBlindScore();
    }

    /**
     * Gets the number of remaining discards available to the player.
     *
     * @return The number of remaining discards
     */
    public int getRemainingDiscards() {
        return state.getRemainingDiscards();
    }

    /**
     * Gets the number of remaining plays available to the player.
     *
     * @return The number of remaining plays
     */
    public int getRemainingPlays() {
        return state.getRemainingPlays();
    }

    /**
     * Gets the player's current hand of cards.
     *
     * @return The player's hand
     */
    public HoldingHand getPlayerHand() {
        return state.getPlayerHand();
    }

    /**
     * Gets the player's current hand of cards as a list.
     *
     * @return The player's hand as a list of cards
     */
    public List<Card> getPlayerHandCards() {
        return state.getPlayerHandCards();
    }

    /**
     * Gets the player's current held jokers.
     *
     * @return The player's jokers
     */
    public HeldJokers getPlayerJokers() {
        return state.getPlayerJokers();
    }

    /**
     * Checks if the game is lost based on game rules.
     *
     * @return true if the game is lost, false otherwise
     */
    public boolean isLost() {
        if (state.getRemainingPlays() <= 0 && !isWon()) {
            return true;
        }
        // Game ends if no plays are remaining
        return false;
    }

    /**
     * Checks if the round was won by the player.
     *
     * @return true if the player won the round, false otherwise
     */
    public boolean isWon() {
        return state.getCurrentScore() >= config.getBlindScore();
    }

    /**
     * Checks if the round is over based on game rules.
     *
     * @return true if the game is lost, false otherwise
     */
    public boolean isRoundOver() {
        // Round ends if no plays are remaining
        return state.getRemainingPlays() <= 0 | isWon();
    }

    /**
     * Gets the display name of this round.
     *
     * @return The round name
     */
    public String getRoundName() {
        return config.getRoundName();
    }

    /**
     * Sets the display name of this round.
     *
     * @param roundName The new round name
     */
    public void setRoundName(String roundName) {
        config.setRoundName(roundName);
    }

    /**
     * Gets the description of this round.
     *
     * @return The round description
     */
    public String getRoundDescription() {
        return config.getRoundDescription();
    }

    /**
     * Sets the description of this round.
     *
     * @param roundDescription The new round description
     */
    public void setRoundDescription(String roundDescription) {
        config.setRoundDescription(roundDescription);
    }

    /**
     * Gets the state object for this round.
     *
     * @return The round state object
     */
    public RoundState getState() {
        return state;
    }

    public void updatePlays(int plays) {
        state.setRemainingPlays(plays);
    }

    public void updateDiscards(int discards) {
        state.setRemainingDiscards(discards);
    }

    /**
     * Gets the configuration object for this round.
     *
     * @return The round configuration object
     */
    public RoundConfig getConfig() {
        return config;
    }

    /**
     * Gets the observable object for this round.
     *
     * @return The round observable object
     */
    public RoundObservable getObservable() {
        return observable;
    }

    /**
     * Gets the boss type for this round.
     *
     * @return The boss type
     */
    public BossType getBossType() {
        return config.getBossType();
    }

    /**
     * Sets the boss type for this round.
     *
     * @param bossType The new boss type
     */
    private void setBossType(BossType bossType) {
        config.setBossType(bossType);
        applyBossVariants();
    }

    /**
     * Gets the deck used in this round.
     *
     * @return The deck object
     */
    public Deck getDeck() {
        return state.getDeck();
    }

    /**
     * Gets the played cards in this round.
     *
     * @return The list of played cards
     */
    public List<Card> getPlayedCards() {
        return state.getChosenCards();
    }

    /**
     * Gets the played hand of cards in this round.
     *
     * @return The poker hand evaluated from the played cards
     * @throws JavatroException If the played cards are invalid or empty
     */
    public PokerHand getPlayedHand() throws JavatroException {
        return HandResult.evaluateHand(state.getChosenCards());
    }

    /**
     * {@code @warn} This method will be deprecated in future versions. Reordering of player hands
     * should be done in UI and not in Round class. Updates the player hand.
     *
     * @param playerHandCards The new holding hand to set
     * @throws IllegalArgumentException if the player hand is null
     */
    public void setPlayerHandCards(List<Card> playerHandCards) throws IllegalArgumentException {
        state.setPlayerHandCards(playerHandCards);
    }
}
