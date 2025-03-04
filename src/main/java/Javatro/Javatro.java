package Javatro;

import javatro_manager.JavatroManager;

import javatro_view.JavatroView;

import java.io.IOException;

// Primary view class that handles current view state etc
public class Javatro {
    private static final JavatroView javatroView = new JavatroView(); // View
    private static final JavatroManager javatroManager = new JavatroManager(javatroView);

    public static void main(String[] args) throws IOException {
        javatroManager.startGame();
        //        List<Card> hand =
        //                List.of(
        //                        new Card(Card.Rank.ACE, Card.Suit.HEARTS),
        //                        new Card(Card.Rank.ACE, Card.Suit.HEARTS),
        //                        new Card(Card.Rank.THREE, Card.Suit.HEARTS),
        //                        new Card(Card.Rank.TEN, Card.Suit.HEARTS),
        //                        new Card(Card.Rank.TEN, Card.Suit.HEARTS));
        //
        //        int TotalChips = 0;
        //
        //        for (Card card : hand) {
        //            System.out.println(card + " - Chips: " + card.getChips());
        //            TotalChips += card.getChips();
        //        }
        //
        //        HandResult result = PokerHand.evaluateHand(hand);
        //        System.out.println("Hand: " + result);
        //        TotalChips += result.chips();
        //        int TotalScore = TotalChips * result.multiplier();
        //
        //        System.out.printf(
        //                "\nTotal Score Gained: %s Chips x %d Multiplier = %d\n",
        //                TotalChips, result.multiplier(), TotalScore);
        //        System.out.println("Current Round Score: 320");
        //        System.out.println("Blind Score to beat: 500");
    }
}
