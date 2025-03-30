package javatro.manager.options;

import javatro.core.Deck;
import javatro.core.JavatroCore;
import javatro.core.JavatroException;
import javatro.display.UI;
import javatro.manager.JavatroManager;

public class CheckeredDeckSelectOption implements Option {

    /**
     * Provides a brief description of the command.
     *
     * @return A string describing the command.
     */
    public String getDescription() {
        return "Checkered Deck: Start with 26 Hearts, 26 Spades";
    }

    /**
     * Executes the command to start with a Red Deck.
     *
     * @throws JavatroException if an error occurs during card selection.
     */
    @Override
    public void execute() throws JavatroException {
        Deck.DeckType deckType = Deck.DeckType.CHECKERED;
        JavatroCore.deck = new Deck(deckType);
        JavatroManager.beginGame(deckType);
        JavatroManager.setScreen(UI.getBlindScreen());
    }
}
