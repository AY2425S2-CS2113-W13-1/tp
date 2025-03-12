package Javatro;

import java.util.List;

public class Round {
    private static final int DEFAULT_BLIND_SCORE = 100;
    private static final int INITIAL_HAND_SIZE = 8;
    private static final int POKER_HAND_SIZE = 5;
    private static final int MAX_PLAYS_PER_ROUND = 3;
    private static final int MAX_DISCARDS_PER_ROUND = 3;

    private int currentScore;
    private int blindScore;
    private int remainingDiscards;
    private int remainingPlays;
    private Deck deck;
    private PokerHand pokerHand;
    private HoldingHand playerHand;
    private State gameState;
    private Ui ui;

    /**
     * Constructs a new round with the specified blind score. The blind score can be fetched from a
     * file or manually inputed.
     *
     * @param gameState The current state of the game.
     */
    public Round(State gameState) {
        this.gameState = gameState;
        this.currentScore = 0;
        this.blindScore = gameState.getBlindScore();
        this.remainingDiscards = MAX_DISCARDS_PER_ROUND;
        this.remainingPlays = gameState.getPlaysPerRound();
        this.deck = gameState.getDeck();
        this.pokerHand = new PokerHand();
        this.playerHand = new HoldingHand();
        this.ui = new Ui();

        // Initial draw
        playerHand.draw(INITIAL_HAND_SIZE);
    }

    /**
     * Plays a set of 5 cards as a poker hand.
     *
     * @param cardIndices Indices of cards to play from the holding hand (must be exactly 5 cards)
     * @throws IllegalArgumentException If the number of cards to play is not exactly 5
     */
    public void playCards(int[] cardIndices) {
        if (cardIndices.length != POKER_HAND_SIZE) {
            throw new IllegalArgumentException("Must play exactly " + POKER_HAND_SIZE + " cards");
        }

        List<Card> playedCards = playerHand.playCards(cardIndices);
        HandResult result = PokerHand.evaluateHand(playedCards);
        Integer totalChips = result.chips();

        for (Card card : playedCards) {
            totalChips += card.getChips();
        }

        // Update round state
        currentScore += totalChips * result.multiplier();
        ui.printHandResult(result, totalChips, currentScore);
        ui.printRoundScore(currentScore, blindScore);

        // Draw new cards to replace played ones
        playerHand.draw(POKER_HAND_SIZE);

        remainingPlays--;
    }

    /**
     * Discards cards from the player's hand.
     *
     * @param cardIndices Indices of cards to discard from the holding hand
     * @throws IllegalStateException If no remaining discards are available
     */
    public void discardCards(int[] cardIndices) {
        if (remainingDiscards <= 0) {
            throw new IllegalStateException("No remaining discards available");
        }
        playerHand.discardCards(cardIndices);
        remainingDiscards--;

        playerHand.draw(cardIndices.length);
        ui.printDiscardResult(playerHand, remainingDiscards);
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

    /**
     * Checks if the round is over based on game rules.
     *
     * @return true if the round is over, false otherwise
     */
    public boolean isRoundOver() {
        // Round ends if no plays are remaining or score exceeds blind score
        return remainingPlays <= 0 || this.isWon();
    }

    /**
     * Determines if the round was won.
     *
     * @return true if player won the round, false otherwise
     */
    public boolean isWon() {
        return currentScore > blindScore;
    }
}
