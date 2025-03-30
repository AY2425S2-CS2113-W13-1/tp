package javatro.display.screens;

import static javatro.display.UI.printANSI;

import javatro.core.JavatroException;
import javatro.manager.options.ExitGameOption;
import javatro.manager.options.HelpMenuOption;
import javatro.manager.options.StartGameOption;

/**
 * The {@code StartScreen} class represents the initial menu screen of the application. It provides
 * options to start a game, access help, or exit the game.
 *
 * <p>This screen displays a visually appealing welcome message, including the javatro logo, which
 * is loaded from an external file during class initialization.
 */
public class StartScreen extends Screen {

    /**
     * Constructs a {@code StartScreen} and initializes available commands.
     *
     * @throws JavatroException if the options title is invalid or if there is an error initializing
     *     the screen
     */
    public StartScreen() throws JavatroException {
        super("MAIN MENU");
        commandMap.add(new StartGameOption());
        commandMap.add(new HelpMenuOption());
        commandMap.add(new ExitGameOption());
    }

    /** Displays the start screen, including the game logo and available options. */
    @Override
    public void displayScreen() {
        printANSI("javatro_logo.txt");
    }
}
