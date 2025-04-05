/**
 * The {@code MainMenuOption} class represents a command that loads the start screen, allowing
 * players to navigate to the main menu.
 */
package javatro.manager.options;

import javatro.core.JavatroCore;
import javatro.core.JavatroException;
import javatro.display.UI;
import javatro.manager.JavatroManager;
import javatro.storage.Storage;

/** A command that loads the run selection screen when executed. */
public class StartRunNumberOption implements Option {

    private int runNumber = 0;
    /**
     * Provides a brief description of the command.
     *
     * @return A string describing the command.
     */
    @Override
    public String getDescription() {
        runNumber = UI.getRunSelectScreen().getRunNumber();
        return "Start With Run " + runNumber;
    }

    /** Executes the command to change the screen to the start menu. */
    @Override
    public void execute() throws JavatroException {
        //Update Storage with chosen run number
        Storage.getStorageInstance().setRunChosen(runNumber);
        JavatroManager.beginGame((Storage.DeckFromKey(Storage.getStorageInstance().getValue(Storage.getStorageInstance().getRunChosen()-1, 8))));

        JavatroManager.jc.beginGame();
        JavatroCore.currentRound.addPropertyChangeListener(javatro.display.UI.getGameScreen());
        JavatroCore.currentRound.updateRoundVariables();
        JavatroManager.setScreen(UI.getGameScreen());
    }

}
