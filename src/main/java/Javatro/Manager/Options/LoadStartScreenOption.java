/**
 * The {@code LoadStartScreenOption} class represents a command that loads the start screen,
 * allowing players to navigate to the main menu.
 */
package Javatro.Manager.Options;

import Javatro.Core.JavatroException;
import Javatro.Display.UI;
import Javatro.Manager.JavatroManager;

/** A command that loads the main menu (start screen) when executed. */
public class LoadStartScreenOption implements Option {

    /** Constructs a {@code LoadStartScreenOption}. */
    public LoadStartScreenOption() {}

    /** Executes the command to change the screen to the start menu. */
    @Override
    public void execute() throws JavatroException {
        JavatroManager.setScreen(UI.getStartScreen());
    }

    /**
     * Provides a brief description of the command.
     *
     * @return A string describing the command.
     */
    @Override
    public String getDescription() {
        return "Main Menu";
    }
}
