/**
 * The {@code StartGameOption} class represents a command that initializes and starts a new game
 * session by setting the game screen.
 */
package javatro.manager.options;

import javatro.core.JavatroCore;
import javatro.core.JavatroException;
import javatro.display.UI;
import javatro.manager.JavatroManager;
import javatro.storage.Storage;

import java.util.List;
import java.util.TreeMap;

import static javatro.storage.Storage.getStorageInstance;

/** A command that starts the game and loads the game screen. */
public class StartGameOption implements Option {

    private String description = "Start Game";  // Default description


    /**
     * Provides a brief description of the command.
     *
     * @return A string describing the command.
     */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     * Updates the description of the command.
     *
     * @param description The new description to set.
     */
    @Override
    public void setDescription(String description) {
        this.description = description;
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
        if (JavatroCore.currentRound == null || JavatroCore.currentRound.isLost()) {
            if(JavatroCore.currentRound == null) Storage.getStorageInstance().addNewRun();
            JavatroCore.currentRound = null;
            JavatroManager.setScreen(UI.getDeckSelectScreen());
        }
        if (JavatroCore.currentRound != null) {
            JavatroManager.setScreen(UI.getGameScreen());
        }
    }

}
