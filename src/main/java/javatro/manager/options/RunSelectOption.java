/**
 * The {@code MainMenuOption} class represents a command that loads the start screen, allowing
 * players to navigate to the main menu.
 */
package javatro.manager.options;

import javatro.core.JavatroException;
import javatro.display.UI;
import javatro.manager.JavatroManager;

/** A command that loads the run selection screen when executed. */
public class RunSelectOption implements Option {

    String description = "Select a Run";

    /**
     * Provides a brief description of the command.
     *
     * @return A string describing the command.
     */
    @Override
    public String getDescription() {
        return description;
    }

    /** Executes the command to change the screen to the start menu. */
    @Override
    public void execute() throws JavatroException {
        UI.reloadRunSelectScreen();
        UI.reloadJumpToRunScreen();
        JavatroManager.setScreen(UI.getRunSelectScreen()); // New Game
    }
}
