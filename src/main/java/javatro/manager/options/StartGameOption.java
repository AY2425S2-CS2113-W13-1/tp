/**
 * The {@code StartGameOption} class represents a command that initializes and starts a new game
 * session by setting the game screen.
 */
package javatro.manager.options;

import javatro.core.JavatroCore;
import javatro.core.JavatroException;
import javatro.display.UI;
import javatro.manager.JavatroManager;

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
     * Executes the command to begin the game, restore available commands, and update the main
     * screen to display the game interface.
     *
     * @throws JavatroException If an error occurs while starting the game.
     */
    @Override
    public void execute() throws JavatroException {
        // Return to game if there is an existing game.
        if (JavatroCore.currentRound != null) {
            JavatroManager.setScreen(UI.getGameScreen());
        }
        JavatroManager.setScreen(UI.getDeckSelectScreen());
    }
}
