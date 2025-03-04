package Javatro;

import java.util.ArrayList;
import java.util.List;

/*
 * Tracks the 8 cards held in a hand
 * Handles the draw function and discard function
 */

public class HoldingHand {
    protected List<Card> Hand;
    private final int HOLDINGLIMIT = 8; // The maximum number of cards a hand can hold

    public HoldingHand(Deck deck) {
        Hand = new ArrayList<Card>();
        for (int i = 0; i < HOLDINGLIMIT; i++) {
            Hand.add(deck.draw());
        }
    }

    /**
     * Returns the specified cards as requested by the player Hand will then draw the same number of
     * cards back from the deck
     *
     * <p>This function should not be called if there are no cards played.
     *
     * @param plays List containing cards to be played
     * @param deck Deck containing the remaining cards
     */
    public List<Card> play(List<Integer> plays, Deck deck) {
        List<Card> playList = new ArrayList<Card>();
        for (int play : plays) {
            Card card = Hand.remove(play);
            playList.add(card);
        }

        for (int i = 0; i < plays.size(); i++) {
            Card tempCard = deck.draw();
            Hand.add(tempCard);
        }
        return playList;
    }

    /**
     * Discards a set number of cards. This function should not be used if the number of discards
     * left is 0.
     *
     * @param discards List containing the cards at specified positions to be discarded
     * @param deck Deck containing the remaining cards
     */
    public void discard(List<Integer> discards, Deck deck) {
        for (int discard : discards) {
            Hand.remove(discard);
        }

        // Draw the same number of cards discarded
        for (int i = 0; i < discards.size(); i++) {
            Card tempCard = deck.draw();
            Hand.add(tempCard);
        }
    }
}
