package Javatro.UI.Screens;

import Javatro.Manager.Options.MakeSelectionOption;

/**
 * The {@code SelectCardsToDiscardScreen} class represents a screen where the user selects cards to
 * discard. It extends {@code SelectionScreen} and includes commands for making a selection.
 */
public class SelectCardsToDiscardScreen extends SelectionScreen {

    /** Constructs a {@code SelectCardsToDiscardScreen} and initializes the selection command. */
    public SelectCardsToDiscardScreen() {
        super.commandMap.add(new MakeSelectionOption(-1));
    }

    /** Displays the current cards in the user's holding hand for selection. */
    @Override
    public void displayScreen() {
        super.displayCardsInHoldingHand();
    }
}
