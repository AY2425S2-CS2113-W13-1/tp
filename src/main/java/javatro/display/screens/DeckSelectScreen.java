package javatro.display.screens;

import javatro.core.Deck;
import javatro.core.JavatroException;
import javatro.manager.options.*;

/**
 * The {@code DeckSelectScreen} class represents the game screen where players can select the
 * desired deck they want.
 */
public class DeckSelectScreen extends Screen {

    /**
     * Constructs a {@code StartScreen} and initializes available commands.
     *
     * @throws JavatroException if the options title is invalid or if there is an error initializing
     *     the screen
     */
    public DeckSelectScreen() throws JavatroException {
        super("Select Your Deck");
        super.commandMap.add(
                new DeckSelectOption("Red Deck: +1 Discards per Round", Deck.DeckType.RED));
        super.commandMap.add(
                new DeckSelectOption("Blue Deck: +1 Hands per Round", Deck.DeckType.BLUE));
        super.commandMap.add(
                new DeckSelectOption(
                        "Checkered Deck: Start with 26 Hearts, 26 Spades",
                        Deck.DeckType.CHECKERED));
        super.commandMap.add(
                new DeckSelectOption(
                        "Abandoned Deck: Start with no Face Cards (K, Q, J)",
                        Deck.DeckType.ABANDONED));
    }

    /** Displays the Deck Select screen. */
    @Override
    public void displayScreen() {}
}
