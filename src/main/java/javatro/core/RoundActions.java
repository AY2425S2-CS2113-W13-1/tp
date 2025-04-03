package javatro.core;

import static javatro.core.Round.DEFAULT_MAX_HAND_SIZE;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Implements the game actions available in a round. */
public class RoundActions {
    /** The round instance associated with these actions. */
    private final Round round;

    /**
     * Creates a new set of actions for the given round.
     *
     * @param round The round to operate on
     */
    public RoundActions(Round round) {
        this.round = round;
    }

    /**
     * Plays a set of cards as a poker hand.
     *
     * @param cardIndices Indices of cards to play from the holding hand
     * @throws JavatroException If the play is invalid
     */
    public List<Card> playCards(List<Integer> cardIndices) throws JavatroException {
        assert cardIndices != null : "Card indices cannot be null";

        validatePlayCards(cardIndices);

        RoundState state = round.getState();
        long oldScore = state.getCurrentScore();
        int oldRemainingPlays = state.getRemainingPlays();

        // Execute play
        List<Card> playedCards = round.getPlayerHand().play(cardIndices);
        PokerHand result = HandResult.evaluateHand(playedCards);
        Score handScore = new Score(round.getBossType());
        long pointsEarned = handScore.getScore(result, playedCards, round.getPlayerJokers());
        state.addScore(pointsEarned);

        // Draw new cards to replace played ones
        round.getPlayerHand().draw(cardIndices.size(), round.getDeck());
        state.decrementPlays();

        assert state.getRemainingPlays() == oldRemainingPlays - 1
                : "Remaining plays should decrease by exactly 1";
        assert state.getCurrentScore() >= oldScore
                : "Score should not decrease after playing cards";
        assert round.getPlayerHand().getHand().size() == Round.INITIAL_HAND_SIZE
                : "Hand size should be maintained after play";

        round.updateRoundVariables();
        return playedCards;
    }

    /**
     * Validates that a set of cards can be played.
     *
     * @param cardIndices Indices of cards to validate
     * @throws JavatroException If the play is invalid
     */
    private void validatePlayCards(List<Integer> cardIndices) throws JavatroException {
        Boolean isAcceptableHandSize =
                cardIndices.size() <= round.getConfig().getMaxHandSize()
                        && cardIndices.size() >= round.getConfig().getMinHandSize();

        if (!isAcceptableHandSize) {
            throw JavatroException.invalidPlayedHand(
                    round.getConfig().getMinHandSize(), round.getConfig().getMaxHandSize());
        }

        if (round.getState().getRemainingPlays() <= 0) {
            throw JavatroException.noPlaysRemaining();
        }

        assert !cardIndices.isEmpty() : "Card indices cannot be empty";
        assert cardIndices.size() <= round.getConfig().getMaxHandSize()
                : "Cannot play more than " + round.getConfig().getMaxHandSize() + " cards";
        assert round.getState().getRemainingPlays() > 0
                : "No plays remaining to execute this action";
    }

    /**
     * Discards cards from the player's hand.
     *
     * @param cardIndices Indices of cards to discard from the holding hand
     * @throws JavatroException If the discard is invalid
     */
    public List<Card> discardCards(List<Integer> cardIndices) throws JavatroException {
        assert cardIndices != null : "Card indices cannot be null";

        validateDiscardCards(cardIndices);

        // Handle duplicates by using a Set
        Set<Integer> indicesToDiscard = new HashSet<>(cardIndices);
        int handSizeBefore = round.getPlayerHand().getHand().size();
        int oldRemainingDiscards = round.getState().getRemainingDiscards();

        // Execute discard
        List<Card> discardedCards = round.getPlayerHand().discard(cardIndices);
        round.getState().decrementDiscards();
        round.getPlayerHand().draw(indicesToDiscard.size(), round.getDeck());

        assert round.getState().getRemainingDiscards() == oldRemainingDiscards - 1
                : "Remaining discards should decrease by exactly 1";
        assert round.getPlayerHand().getHand().size() == handSizeBefore
                : "Hand size should be maintained after discard";

        round.updateRoundVariables();
        return discardedCards;
    }

    /**
     * Validates that a set of cards can be discarded.
     *
     * @param cardIndices Indices of cards to validate
     * @throws JavatroException If the discard is invalid
     */
    private void validateDiscardCards(List<Integer> cardIndices) throws JavatroException {
        RoundState state = round.getState();
        int numberOfDiscards = cardIndices.size();

        if (state.getRemainingDiscards() <= 0) {
            throw JavatroException.noRemainingDiscards();
        }

        if (numberOfDiscards > DEFAULT_MAX_HAND_SIZE) {
            throw JavatroException.tooManyDiscards();
        }

        if (cardIndices.size() < 1) {
            throw JavatroException.cannotDiscardZeroCards();
        }

        assert !cardIndices.isEmpty() : "Cannot discard zero cards";
        assert state.getRemainingDiscards() > 0 : "No discards remaining to execute this action";
    }
}
