package javatro.display.screens;

import javatro.core.JavatroException;
import javatro.manager.options.AbandonedDeckSelectOption;
import javatro.manager.options.BlueDeckSelectOption;
import javatro.manager.options.CheckeredDeckSelectOption;
import javatro.manager.options.RedDeckSelectOption;

/**
 * The {@code DeckSelectScreen} class represents the game screen where players can select the
 * desired deck they want.
 */
public class SelectDeckScreen extends Screen {

    /**
     * Constructs a {@code StartScreen} and initializes available commands.
     *
     * @throws JavatroException if the options title is invalid or if there is an error initializing
     *     the screen
     */
    public SelectDeckScreen() throws JavatroException {
        super("Select Your Deck");
        super.commandMap.add(new RedDeckSelectOption());
        super.commandMap.add(new BlueDeckSelectOption());
        super.commandMap.add(new CheckeredDeckSelectOption());
        super.commandMap.add(new AbandonedDeckSelectOption());
    }

    /** Displays the Deck Select screen. */
    @Override
    public void displayScreen() {}
}
