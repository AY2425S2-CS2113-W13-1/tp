package Javatro;

import javatro_core.PokerHand;

public class Ui {

    /**
     * Prints the result of a played hand.
     *
     * @param result The result of the hand
     * @param totalChips The total chips won from the hand
     * @param currentScore The current score after playing the hand
     */
    public void printHandResult(PokerHand result, int totalChips, int currentScore) {
        System.out.println("Hand Result: " + result);
        System.out.println("Total Chips Won: " + totalChips);
        System.out.println("Current Score: " + currentScore);
    }

    /**
     * Prints the current round score.
     *
     * @param currentScore The current score
     * @param blindScore The blind score
     */
    public void printRoundScore(int currentScore, int blindScore) {
        System.out.println("Current Score: " + currentScore);
        System.out.println("Blind Score: " + blindScore);
    }

    /**
     * Prints the result of discarding cards.
     *
     * @param playerHand The player's hand after discarding
     * @param remainingDiscards The remaining number of discards
     */
    public void printDiscardResult(HoldingHand playerHand, int remainingDiscards) {
        System.out.println("Cards after discard: " + playerHand);
        System.out.println("Remaining Discards: " + remainingDiscards);
    }

    /**
     * Prints the player's hand.
     *
     * @param playerHand The player's hand
     */
    public void printPlayerHand(HoldingHand playerHand) {
        // <Skeleton code> to print the actual player's hand in the console
        System.out.println("Your Hand: XXX");
    }
}
