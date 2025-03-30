// @@author swethacool
package javatro.manager.options;

import javatro.core.JavatroException;
import javatro.display.UI;
import javatro.manager.JavatroManager;

import java.util.List;

/**
 * The HelpHowOption class explains how to play javatro. This command is executed when the player
 * requests gameplay instructions.
 */
public class HelpHowOption implements Option {

    /**
     * Returns a description of this command.
     *
     * @return A string describing the command.
     */
    @Override
    public String getDescription() {
        return "How To Play";
    }

    /** Executes the command to display instructions on how to play. */
    @Override
    public void execute() throws JavatroException {
        String title = "♥️ ♠️ 🃏 " + UI.BOLD + "How To Play Javatro" + " 🃏 ♦️ ♣️" + UI.END;

        String[] lines = {
            "Welcome to the Javatro!",
            "Here's how to play and navigate the game:",
            "1. Start a new game with 'start'.",
            "2. Select the blind you desire.",
            "2. Draw cards using 'draw'.",
            "3. Play poker hands to attack enemies.",
            "4. Earn new cards and modify your deck.",
            "5. Survive as long as possible and improve your strategy.",
            "",
            "Use commands like:",
            "   - 'attack'  : Play a hand to deal damage.",
            "   - 'defend'  : Use a card to reduce incoming damage.",
            "   - 'exit'    : Quit the game.",
            "Ante and Blind System:",
            "   - The game uses an ante system to start each round.",
            "   - The ante increases as rounds progress, starting from the initial small blind,"
                    + " large blind, and boss blind.",
            "   - This also means the score to beat for each round increases progressively"
                    + " \uD83D\uDCC8\uD83C\uDFAF",
            "   - The blinds rotate with each new round, increasing as the game progresses.",
            "   - However do take note that the Small and Large Blinds are optional while the Boss"
                    + " blind is compulsory in the game play.",
            "   - Have fun playing! \uD83D\uDE0E\uD83C\uDF89",
        };

        UI.printBorderedContent(title, List.of(lines));
        JavatroManager.setScreen(UI.getHelpScreen());
    }
}
