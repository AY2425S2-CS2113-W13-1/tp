package Javatro;

import java.util.ArrayList;
import java.util.List;

/*
 * Tracks the 8 cards held in a hand
 * Handles the draw function and discard function
 */

public class HoldingHand {
    protected List<Card> Hand;
    private final int HOLDING_LIMIT = 8; // The maximum number of cards a hand can hold

    /** Instantiate an empty List of Cards. */
    public HoldingHand() {
        Hand = new ArrayList<Card>();
    }

    /**
     * Adds a Card to the Hand.
     *
     * <p>This function should not be called if Hand size >= HOLDING_LIMIT
     */
    public void add(Card cardToAdd) {
        if (Hand.size() < HOLDING_LIMIT) {
            Hand.add(cardToAdd);
        } else {
            System.out.println("Exceeded Limit. Card not added.");
        }
    }

    /**
     * Returns the specified cards as requested by the player Hand will then draw the same number of
     * cards back from the deck
     *
     * <p>This function should not be called if there are no cards played.
     *
     * @param cardsToPlay List containing cards to be played
     * @param deck Deck containing the remaining cards
     */
    public List<Card> play(List<Integer> cardsToPlay, Deck deck) {
        if (cardsToPlay.size() > 5) {
            System.out.println(
                    "Number of cards played "
                            + "("
                            + cardsToPlay.size()
                            + ")"
                            + "exceeds the maximum allowed. (5)");
            return null;
        }

        List<Card> playList = new ArrayList<>();
        for (int play : cardsToPlay) {
            Card card = Hand.remove(play);
            playList.add(card);
        }

        for (int i = 0; i < cardsToPlay.size(); i++) {
            Card tempCard = deck.draw();
            Hand.add(tempCard);
        }
        return playList;
    }

    /**
     * Discards a set number of cards. This function should not be used if the number of discards
     * left is 0.
     *
     * @param cardsToDiscard List containing the cards at specified positions to be discarded
     * @param deck Deck containing the remaining cards
     */
    public void discard(List<Integer> cardsToDiscard, Deck deck) {
        for (int discard : cardsToDiscard) {
            Hand.remove(discard);
        }

        // Draw the same number of cards discarded
        for (int i = 0; i < cardsToDiscard.size(); i++) {
            Card tempCard = deck.draw();
            Hand.add(tempCard);
        }
    }
}
