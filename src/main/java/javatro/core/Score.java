package javatro.core;

import javatro.core.jokers.HeldJokers;
import javatro.core.jokers.Joker;

import java.util.ArrayList;
import java.util.List;

/** Contains the algorithm for calculating the final score and played hand. */
public class Score {
    public double totalChips = 0;
    public double totalMultiplier = 0;
    public static List<Card> playedCardsList;
    public ArrayList<Joker> jokerList;

    /**
     * Returns the final score from total chips * total multiplier, after rounding.
     *
     * @param totalChips The chip total of the played hand
     * @param totalMultiplier The multiplier total of the played hand
     */
    private static long calculateFinalScore(double totalChips, double totalMultiplier) {
        return (long) Math.ceil(Math.round(totalChips) * Math.round(totalMultiplier));
    }

    /** Returns the score of the played hand by calculating the value of the hand. */
    public long getScore(PokerHand pokerHand, List<Card> playedCardList, HeldJokers heldJokers)
            throws JavatroException {

        // Highlight any boss blind effects that apply here
        // Cannot play suits, cannot play Face Cards, Must play 5 cards
        jokerList = heldJokers.getJokers();
        playedCardsList = playedCardList;

        // First add pokerHand's chip and mult base to the scores.
        totalChips = pokerHand.getChips();
        totalMultiplier = pokerHand.getMultiplier();

        // Score the cards and apply any Jokers that have effects on play here.
        for (Card currentCard : playedCardList) {
            if (isValidCard(currentCard)) {
                scoreCard(currentCard);
            }
        }

        // From here, check each joker in heldJokers applies post round effects, in the order they
        // are placed.
        for (int i = 0; i < jokerList.size(); i++) {
            Joker currentJoker = jokerList.get(i);
            if (currentJoker.scoreType == Joker.ScoreType.AFTERHANDPLAY) {
                currentJoker.interact(this, null);
            }
        }

        // Round the score and return the correct score value.
        return calculateFinalScore(totalChips, totalMultiplier);
    }

    /**
     * Returns whether the card should be considered for scoring. This boolean will return false if
     * the card falls under boss blind conditions.
     *
     * @param card The card to be checked for validity
     * @param
     */
    private boolean isValidCard(Card card) {
        // To be checked for in boss blind
        // Apply boss blind logic to return this as true or false based on the card's
        // characteristics.
        return true;
    }

    /**
     * Adds the card's value to the score, and check for any joker interactions with the card.
     *
     * @param card The card to be scored
     * @param
     */
    private void scoreCard(Card card) {
        totalChips += card.getChips();
        // From here, check each joker in heldJokers can apply effects on card play, in the
        // order they are placed.
        for (int i = 0; i < jokerList.size(); i++) {
            Joker currentJoker = jokerList.get(i);
            if (currentJoker.scoreType == Joker.ScoreType.ONCARDPLAY) {
                currentJoker.interact(this, card);
            }
        }
    }
}
