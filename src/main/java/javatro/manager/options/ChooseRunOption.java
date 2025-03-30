/**
 * The {@code StartGameOption} class represents a command that initializes and starts a new game
 * session by setting the game screen.
 */
package javatro.manager.options;

import static javatro.storage.Storage.createDeckFromString;

import javatro.core.*;
import javatro.display.UI;
import javatro.manager.JavatroManager;
import javatro.storage.Storage;

import java.util.ArrayList;
import java.util.List;

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
        if(runNumber >= Storage.getInstance().getRunData().size()) return "Start New Run";
        return "Choose Run " + (runNumber + 1);
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

        if (runNumber < Storage.getInstance().getRunData().size() && (JavatroCore.currentRound == null || JavatroCore.currentRound.isLost()) ) {
            JavatroCore.currentRound = null;
//            // New Run
            JavatroCore.deck = new Deck(createDeckFromString(Storage.getInstance().getRunData().get(Storage.chosenRun).get(2)));
            JavatroManager.beginGame(createDeckFromString(Storage.getInstance().getRunData().get(Storage.chosenRun).get(2)));

            List<Card> storedPlayingCards = new ArrayList<>();

            for (int i = 0; i < 8; i++) {
                String card =
                        Storage.getInstance().getRunData().get(Storage.chosenRun).get(i + 3);
                storedPlayingCards.add(Storage.parseCardString(card));
            }

//                JavatroManager.jc.beginGame();
//                JavatroCore.currentRound.addPropertyChangeListener(javatro.display.UI.getGameScreen());
//                JavatroCore.currentRound.getHoldingHand().setHand(storedPlayingCards);
//                JavatroCore.currentRound.updateRoundVariables();
            JavatroManager.setScreen(UI.getBlindScreen());


//                JavatroManager.beginGame(
//                        createDeckFromString(
//                                Storage.getInstance().getRunData().get(Storage.chosenRun).get(2)));
//                JavatroCore.getAnte().setBlind(Ante.Blind.SMALL_BLIND);
//
//                JavatroManager.jc.beginGame();
//                JavatroCore.currentRound.getHoldingHand().setHand(storedPlayingCards);
//                JavatroCore.currentRound.addPropertyChangeListener(
//                        javatro.display.UI.getGameScreen());
//                JavatroCore.currentRound.updateRoundVariables();
//                JavatroManager.setScreen(UI.getGameScreen());
        } else if(runNumber >= Storage.getInstance().getRunData().size()) {
            //Start a normal game
            Storage.chosenRun = runNumber;
            System.out.println("NEW GAME");
            List<String> newGame = new ArrayList<>();
            newGame.add("1");
            newGame.add("1");
            newGame.add("DEFAULT");
            newGame.add("-");
            newGame.add("-");
            newGame.add("-");
            newGame.add("-");
            newGame.add("-");
            newGame.add("-");
            newGame.add("-");
            newGame.add("-");
            newGame.add("\n");
            Storage.addNewRun(newGame);
            JavatroManager.setScreen(UI.getDeckSelectScreen());
        }

//        if (JavatroCore.currentRound != null) {
//            JavatroManager.setScreen(UI.getGameScreen());
//        }
    }

    public void setRunNumber(int runNumber) {
        this.runNumber = runNumber;
    }
}
