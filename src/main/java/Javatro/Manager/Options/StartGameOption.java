/**
 * The {@code StartGameOption} class represents a command that initializes and starts a new
 * game session by setting the game screen.
 */
package Javatro.Manager.Options;

import Javatro.Core.JavatroException;
import Javatro.Manager.JavatroManager;
import Javatro.Display.UI;

/** A command that starts the game and loads the game screen. */
public class StartGameOption implements Option {

    /**
     * Provides a brief description of the command.
     *
     * @return A string describing the command.
     */
    @Override
    public String getDescription() {
        return "Start Game";
    }

    /**
     * Executes the command to begin the game, restore available commands, and update the screen to
     * display the game interface.
     *
     * @throws JavatroException If an error occurs while starting the game.
     */
    @Override
    public void execute() throws JavatroException {
        JavatroManager.beginGame();
        Javatro.Display.UI.getGameScreen().restoreGameCommands();
        // Update the main screen to show the game screen
        JavatroManager.setScreen(UI.getGameScreen());
    }
}
