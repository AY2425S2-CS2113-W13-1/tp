/**
 * The {@code MainMenuOption} class represents a command that loads the start screen, allowing
 * players to navigate to the main menu.
 */
package javatro.manager.options;

import javatro.core.JavatroException;
import javatro.display.UI;
import javatro.manager.JavatroManager;

/** A command that loads the run selection screen when executed. */
public class SelectRunNumberOption implements Option {

    private int runNumber = 0;

    /**
     * Provides a brief description of the command.
     *
     * @return A string describing the command.
     */
    @Override
    public String getDescription() {
        return "Jump to Run " + runNumber;
    }

    /** Executes the command to change the screen to the start menu. */
    @Override
    public void execute() throws JavatroException {
        UI.getRunSelectScreen().setRunNumber(runNumber);
        JavatroManager.setScreen(UI.getRunSelectScreen());
    }

    public void setRunNumber(int runNumber) {
        this.runNumber = runNumber;
    }
}
