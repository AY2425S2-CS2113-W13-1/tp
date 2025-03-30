/**
 * The {@code StartGameOption} class represents a command that initializes and starts a new game
 * session by setting the game screen.
 */
package javatro.manager.options;

import static javatro.storage.Storage.createDeckFromString;

import javatro.core.Ante;
import javatro.core.JavatroCore;
import javatro.core.JavatroException;
import javatro.display.UI;
import javatro.manager.JavatroManager;
import javatro.storage.Storage;

/** A command that starts the game and loads the game screen. */
public class ChooseRunOption implements Option {

    private int runNumber;

    /**
     * Provides a brief description of the command.
     *
     * @return A string describing the command.
     */
    @Override
    public String getDescription() {
        return "Choose Run " + runNumber;
    }

    /**
     * Executes the command to begin the game, restore available commands, and update the main
     * screen to display the game interface.
     *
     * @throws JavatroException If an error occurs while starting the game.
     */
    @Override
    public void execute() throws JavatroException {
        Storage.chosenRun = runNumber;
        if (JavatroCore.currentRound == null || JavatroCore.currentRound.isLost()) {
            JavatroCore.currentRound = null;
            // New Run
            if (runNumber > Storage.getInstance().getRunData().size())
                JavatroManager.setScreen(UI.getDeckSelectScreen());
            else {
                JavatroManager.beginGame(
                        createDeckFromString(
                                Storage.getInstance().getRunData().get(Storage.chosenRun).get(2)));
                JavatroCore.getAnte().setBlind(Ante.Blind.SMALL_BLIND);
                JavatroManager.jc.beginGame();
                JavatroCore.currentRound.addPropertyChangeListener(
                        javatro.display.UI.getGameScreen());
                JavatroCore.currentRound.updateRoundVariables();
                JavatroManager.setScreen(UI.getGameScreen());
            }
        }
        if (JavatroCore.currentRound != null) {
            JavatroManager.setScreen(UI.getGameScreen());
        }
    }

    public void setRunNumber(int runNumber) {
        this.runNumber = runNumber;
    }
}
