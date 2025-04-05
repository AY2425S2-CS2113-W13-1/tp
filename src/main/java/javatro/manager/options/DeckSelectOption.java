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
        JavatroCore.deck = new Deck(deckType);
        Storage.getStorageInstance().setValue(Storage.getStorageInstance().getRunChosen()-1, 8,deckType.getName());
        JavatroManager.beginGame((Storage.DeckFromKey(Storage.getStorageInstance().getValue(Storage.getStorageInstance().getRunChosen(), 8))));
        JavatroManager.setScreen(UI.getBlindScreen());
    }
}
