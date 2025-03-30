package javatro.display.screens;

import javatro.core.JavatroException;

/**
 * The {@code DiscardCardScreen} class represents a screen where the user selects cards to discard.
 * It extends {@code CardSelectScreen} and includes commands for making a selection. The user can
 * select up to a predefined limit of cards (default is 5).
 *
 * @see CardSelectScreen
 */
public class DiscardCardScreen extends CardSelectScreen {

    /**
     * Constructs a {@code DiscardCardScreen} and initializes the selection command. The selection
     * limit is set to 5, meaning a maximum of 5 cards can be selected to discard.
     *
     * @throws JavatroException if an error occurs during initialization.
     */
    public DiscardCardScreen() throws JavatroException {
        super("SELECT CARDS TO DISCARD");
    }

    /**
     * Displays the current cards in the user's holding hand for selection. This method overrides
     * the parent class implementation to provide specific behavior for discarding cards.
     */
    @Override
    public void displayScreen() {
        super.displayHoldingHand();
    }
}
