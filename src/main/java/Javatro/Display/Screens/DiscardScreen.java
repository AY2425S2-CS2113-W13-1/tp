package Javatro.Display.Screens;

import Javatro.Core.JavatroException;
import Javatro.Manager.Options.MakeSelectionOption;

/**
 * The {@code DiscardScreen} class represents a screen where the user selects cards to discard. It
 * extends {@code SelectionScreen} and includes commands for making a selection.
 */
public class DiscardScreen extends SelectionScreen {

    /** Constructs a {@code DiscardScreen} and initializes the selection command. */
    public DiscardScreen() throws JavatroException {
        super();
        super.commandMap.add(new MakeSelectionOption(-1));
    }

    /** Displays the current cards in the user's holding hand for selection. */
    @Override
    public void displayScreen() {
        super.displayCardsInHoldingHand();
    }
}
