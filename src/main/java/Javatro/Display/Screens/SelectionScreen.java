package Javatro.Display.Screens;

import Javatro.Core.Card;
import Javatro.Core.JavatroCore;
import Javatro.Core.JavatroException;
import Javatro.Manager.Options.ResumeGameOption;

import java.util.List;

/**
 * The {@code SelectionScreen} class represents an abstract screen where users select cards from
 * their hand. It provides methods for updating and displaying the player's current hand. (Used for
 * the DiscardScreen and PlayScreen)
 */
public class SelectionScreen extends Screen {

    /** The limit on the number of cards that can be selected. */
    protected int SELECTION_LIMIT;

    /** The list of cards currently in the player's hand. */
    List<Card> holdingHand;

    /** Constructs a {@code SelectionScreen} and initializes it with a resume game command. */
    public SelectionScreen() throws JavatroException {
        super("SELECT CARDS");
        commandMap.add(new ResumeGameOption());
    }

    /** Updates the holding hand by retrieving the player's current hand from the game core. */
    public void updateHoldingHand() {
        holdingHand = JavatroCore.currentRound.getPlayerHand();
    }

    /** Displays the player's current hand of cards in a formatted layout. */
    protected void displayCardsInHoldingHand() {
        updateHoldingHand();
        System.out.println("YOUR CARDS:");

        String numberHeaders = "";
        for (int i = 0; i < holdingHand.size(); i++) {
            numberHeaders +=
                    "|" + GameScreen.getDisplayStringCenter(Integer.toString(i + 1), 5) + "|    ";
        }

        String cardHeaders = (GameScreen.getHeaderString(5) + "    ").repeat(holdingHand.size());
        System.out.println(numberHeaders);
        System.out.println(cardHeaders);

        String cardValues = "";
        for (int i = 0; i < holdingHand.size(); i++) {
            cardValues +=
                    "|"
                            + GameScreen.getDisplayStringCenter(
                                    holdingHand.get(i).getSimplified(), 5)
                            + "|    ";
        }

        System.out.println(cardValues);
        System.out.println(cardHeaders);
    }

    /** Displays the screen. This method is overridden by subclasses. */
    @Override
    public void displayScreen() {}
}
