/**
 * The {@code ResumeGameOption} class represents a command that allows the player to return to the
 * main game screen after navigating away.
 */
package javatro.manager.options;

import javatro.core.JavatroException;
import javatro.display.UI;
import javatro.manager.JavatroManager;

/** A command that enables players to return to the game screen. */
public class ResumeGameOption implements Option {

    /**
     * Provides a brief description of the command.
     *
     * @return A string describing the command.
     */
    @Override
    public String getDescription() {
        return "Return To Game";
    }

    /**
     * Executes the resume game command, updating the game screen to return the player to the main
     * game interface.
     *
     * @throws JavatroException If an error occurs during execution.
     */
    @Override
    public void execute() throws JavatroException {

        JavatroManager.setScreen(UI.getGameScreen());
    }
}
