/**
 * The {@code MainMenuOption} class represents a command that loads the start screen, allowing
 * players to navigate to the main menu.
 */
package javatro.manager.options;

import javatro.core.JavatroException;
import javatro.display.UI;
import javatro.manager.JavatroManager;
import javatro.storage.Storage;

/** A command that loads the run selection screen when executed. */
public class SeeNextRunOption implements Option {

    /**
     * Provides a brief description of the command.
     *
     * @return A string describing the command.
     */
    @Override
    public String getDescription() {
        return "See Next Run";
    }

    /** Executes the command to change the screen to the start menu. */
    @Override
    public void execute() throws JavatroException {

        int currentRun = UI.getRunSelectScreen().getRunNumber();
        int totalRuns = Storage.getStorageInstance().getNumberOfRuns();
        int nextRun = (currentRun % totalRuns) + 1;
        UI.getRunSelectScreen().setRunNumber(nextRun);

        JavatroManager.setScreen(UI.getRunSelectScreen());
    }
}
