package javatro.manager.options;

import javatro.core.Deck;
import javatro.core.JavatroCore;
import javatro.core.JavatroException;
import javatro.display.UI;
import javatro.manager.JavatroManager;
import javatro.storage.Storage;

public class DeckSelectOption implements Option {

    private final String description;
    private final Deck.DeckType deckType;

    /**
     * Constructs a DeckSelectOption with a custom description and deck.
     *
     * @param description The description of the deck option.
     * @param deckType The deck type associated with this option.
     */
    public DeckSelectOption(String description, Deck.DeckType deckType) {
        this.description = description;
        this.deckType = deckType;
    }

    /**
     * Provides a brief description of the command.
     *
     * @return A string describing the command.
     */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     * Executes the command to start the game with the selected deck.
     *
     * @throws JavatroException if an error occurs during execution.
     */
    @Override
    public void execute() throws JavatroException {
        // deckType = Storage.chosenRun < Storage.getInstance().getRunData().size() ?
        // createDeckFromString(Storage.getInstance().getRunData().get(Storage.chosenRun).get(2)) :
        // deckType;
        JavatroCore.deck = new Deck(deckType);
        // Update Storage with latest deck (for new run)
        Storage.getInstance().getRunData().get(Storage.chosenRun).set(2, deckType.getName());
        JavatroManager.beginGame(deckType);
        JavatroManager.setScreen(UI.getBlindScreen());
    }
}
