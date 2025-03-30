package javatro.manager.options;

import javatro.core.JavatroException;
import javatro.display.UI;
import javatro.manager.JavatroManager;

/**
 * The {@code PlayCardOption} class represents a command that allows the player to select cards to
 * play during the game. It updates the game screen to display the card selection interface for
 * playing cards.
 */
public class PlayCardOption implements Option {

    /**
     * Provides a brief description of the command.
     *
     * @return A string describing the command.
     */
    @Override
    public String getDescription() {
        return "Play Cards";
    }

    /**
     * Executes the play cards command, updating the game screen to the "Select Cards to Play"
     * interface.
     *
     * @throws JavatroException if an error occurs during execution.
     */
    @Override
    public void execute() throws JavatroException {
        JavatroManager.setScreen(UI.getPlayScreen());
    }
}
