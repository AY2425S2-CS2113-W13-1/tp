package javatro.core;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
 * Tracks the 8 cards held in a hand
 * Handles the draw function and discard function
 */

public class HoldingHand {
    public List<Card> Hand;
    private int HOLDING_LIMIT; // The maximum number of cards a hand can hold

    /** Instantiate an empty List of Cards. */
    public HoldingHand() {
        Hand = new ArrayList<Card>();
        this.HOLDING_LIMIT = 8;
    }

    /**
     * Adds a Card to the Hand.
     *
     * <p>This function should not be called if Hand size >= HOLDING_LIMIT
     */
    public void add(Card cardToAdd) throws JavatroException {
        if (Hand.size() < HOLDING_LIMIT) {
            Hand.add(cardToAdd);
        } else {
            throw new JavatroException("Exceeded Cards to be added.");
        }
    }

    /**
     * Draws a specified number of cards from the deck and adds them to the Hand.
     *
     * @param numberOfDraws The number of cards to draw from the deck
     * @param deck Deck containing the remaining cards
     */
    public void draw(int numberOfDraws, Deck deck) throws JavatroException {
        for (int i = 0; i < numberOfDraws; i++) {
            if (Hand.size() < HOLDING_LIMIT) {
                Card tempCard = deck.draw();
                Hand.add(tempCard);
            } else {
                throw new JavatroException("Exceeded Cards to be added.");
            }
        }
    }

    /**
     * Returns the specified cards as requested by the player Hand will then draw the same number of
     * cards back from the deck
     *
     * <p>This function should not be called if there are no cards played.
     *
     * @param cardsToPlay List containing cards to be played
     */
    public List<Card> play(List<Integer> cardsToPlay) throws JavatroException {
        List<Card> playList = new ArrayList<>();

        // Validate that cardsToPlay and the played card positions are valid inputs
        if (cardsToPlay.size() > 5) {
            throw new JavatroException(
                    "Number of cards played "
                            + "("
                            + cardsToPlay.size()
                            + ")"
                            + " exceeds maximum allowed. (5)");
        } else {
            for (int index : cardsToPlay) {
                if (index < 0 || index >= Hand.size()) {
                    throw new JavatroException("Invalid index in cards to be played: " + index);
                }
            }
        }

        // Create a set to mark indices for removal
        Set<Integer> indicesToRemove = new HashSet<>(cardsToPlay);

        // Add cards that should be played in order of cardsToPlay
        for (int index : cardsToPlay) {
            if (indicesToRemove.contains(index)) {
                Card card = Hand.get(index);
                playList.add(card);
            }
        }

        // Remove the cardsToPlay from the Hand in descending order of indices
        List<Integer> sortedToRemove = new ArrayList<>(indicesToRemove);
        sortedToRemove.sort(Comparator.reverseOrder());
        for (int index : sortedToRemove) {
            Hand.remove(index);
        }

        return playList;
    }

    /**
     * Discards a set number of cards. This function should not be used if the number of discards
     * left is 0.
     *
     * @param cardsToDiscard List containing the cards at specified positions to be discarded
     */
    public List<Card> discard(List<Integer> cardsToDiscard) throws JavatroException {
        List<Card> discardList = new ArrayList<>();

        // Validate that cardsToDiscard and the played card positions are valid inputs
        if (cardsToDiscard.size() > 5) {
            throw new JavatroException(
                    "Number of cards discarded "
                            + "("
                            + cardsToDiscard.size()
                            + ")"
                            + " exceeds maximum allowed. (5)");
        } else {
            for (int index : cardsToDiscard) {
                if (index < 0 || index >= Hand.size()) {
                    throw new JavatroException("Invalid index in cards to be discarded: " + index);
                }
            }
        }

        // Store the cards to be discarded
        Set<Integer> indicesToDiscard = new HashSet<>(cardsToDiscard);
        for (int index : indicesToDiscard) {
            discardList.add(Hand.get(index)); // Add card to discard list before removing
        }

        // Remove the cardsToPlay from the Hand in descending order of indices
        List<Integer> sortedToRemove = new ArrayList<>(indicesToDiscard);
        sortedToRemove.sort(Comparator.reverseOrder());
        for (int index : sortedToRemove) {
            Hand.remove(index);
        }

        return discardList;
    }

    /**
     * Sorts the hand first by suit (Spades > Hearts > Clubs > Diamonds), then by rank (Ace > King >
     * Queen > ... > Two) within each suit.
     */
    public void sortBySuit() {
        Hand.sort(
                Comparator.comparingInt(
                                (Card card) ->
                                        switch (card.suit()) {
                                            case SPADES -> 0;
                                            case HEARTS -> 1;
                                            case CLUBS -> 2;
                                            case DIAMONDS -> 3;
                                            default -> 4; // shouldn't happen
                                        })
                        .thenComparingInt(card -> -card.rank().ordinal()));
    }

    /**
     * Sorts the hand first by rank (Ace > King > Queen > ... > Two), then by suit (Spades > Hearts
     * > Clubs > Diamonds) within each rank.
     */
    public void sortByRank() {
        Hand.sort(
                Comparator.comparingInt((Card card) -> -card.rank().ordinal())
                        .thenComparingInt(
                                card ->
                                        switch (card.suit()) {
                                            case SPADES -> 0;
                                            case HEARTS -> 1;
                                            case CLUBS -> 2;
                                            case DIAMONDS -> 3;
                                            default -> 4; // shouldn't happen
                                        }));
    }

    /**
     * Retrieves the list of cards in the player's hand.
     *
     * @return A {@code List} of {@code Card} objects representing the player's hand.
     */
    public List<Card> getHand() {
        return Hand;
    }

    // Sets input as current hand
    public void setHand(List<Card> hand) {
        this.Hand = hand;
    }

    /**
     * Change the HOLDING_LIMIT of the HoldingHand Class. This function is used when you need to
     * manually change the HOLDING_LIMIT
     */
    public void setHOLDINGLIMIT(int holdingLimit) {
        HOLDING_LIMIT = holdingLimit;
    }
}
