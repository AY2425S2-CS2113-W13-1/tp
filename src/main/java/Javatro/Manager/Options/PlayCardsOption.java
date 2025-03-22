/**
 * The {@code PlayCardsOption} class represents a command that allows the player to select cards to
 * play during the game. It updates the game screen to display the card selection interface for
 * playing cards.
 */
package Javatro.Manager.Options;

import Javatro.Core.JavatroException;
import Javatro.Display.UI;
import Javatro.Manager.JavatroManager;

/** A command that enables players to select and play cards. */
public class PlayCardsOption implements Option {

    /**
     * Executes the play cards command, updating the game screen to the "Select Cards to Play"
     * interface.
     *
     * @throws JavatroException If an error occurs during execution.
     */
    @Override
    public void execute() throws JavatroException {
        // Update the main screen to show select cards to play screen
        JavatroManager.setScreen(UI.getPlayScreen());
    }

    /**
     * Provides a brief description of the command.
     *
     * @return A string describing the command.
     */
    @Override
    public String getDescription() {
        return "Play Cards";
    }
}
