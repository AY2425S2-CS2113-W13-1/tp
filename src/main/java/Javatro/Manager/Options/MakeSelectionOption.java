/**
 * The {@code MakeSelectionOption} class represents a command that allows the player to make a
 * selection of cards to either play or discard, based on the current game screen.
 */
package Javatro.Manager.Options;

import Javatro.Core.JavatroCore;
import Javatro.Core.JavatroException;
import Javatro.Display.Screens.DiscardScreen;
import Javatro.Display.Screens.PlayScreen;
import Javatro.Display.UI;
import Javatro.Manager.JavatroManager;
import Javatro.Manager.Parser;

import java.util.List;

/** A command that enables players to make a selection of cards to play or discard. */
public class MakeSelectionOption implements Option {

    /** The maximum number of cards that can be selected. */
    private final int LIMIT;

    /**
     * Constructs a {@code MakeSelectionOption} with a specified selection limit.
     *
     * @param LIMIT The maximum number of cards that can be selected (-1 for unlimited selection).
     */
    public MakeSelectionOption(int LIMIT) {
        this.LIMIT = LIMIT;
    }

    /**
     * Provides a brief description of the command.
     *
     * @return A string describing the command.
     */
    @Override
    public String getDescription() {
        return "Make Selection";
    }

    /**
     * Executes the selection command, prompting the player to select cards to either play or
     * discard. The behavior changes based on the current screen.
     *
     * @throws JavatroException If an error occurs during card selection.
     */
    @Override
    public void execute() throws JavatroException {
        List<Integer> userInput;

        if (LIMIT == -1) {
            userInput =
                    Parser.getCardInput(
                            JavatroCore.currentRound.getPlayerHand().size(),
                            JavatroCore.currentRound.getPlayerHand().size());
        } else {
            userInput = Parser.getCardInput(JavatroCore.currentRound.getPlayerHand().size(), LIMIT);
        }

        if (Javatro.Display.UI.getCurrentScreen() instanceof PlayScreen) {
            // Select and play the chosen cards
            JavatroCore.currentRound.playCards(userInput);
        } else if (UI.getCurrentScreen() instanceof DiscardScreen) {
            // Discard the selected cards
            JavatroCore.currentRound.discardCards(userInput);
        }

        // Return to the game screen after selection
        JavatroManager.setScreen(Javatro.Display.UI.getGameScreen());
    }
}
