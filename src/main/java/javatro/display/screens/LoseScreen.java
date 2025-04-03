// @@author Markneoneo
package javatro.display.screens;

import static javatro.display.UI.BLACK_B;
import static javatro.display.UI.BOLD;
import static javatro.display.UI.BORDER_WIDTH;
import static javatro.display.UI.BOTTOM_LEFT;
import static javatro.display.UI.BOTTOM_RIGHT;
import static javatro.display.UI.END;
import static javatro.display.UI.HORIZONTAL;
import static javatro.display.UI.ITALICS;
import static javatro.display.UI.RED;
import static javatro.display.UI.TOP_LEFT;
import static javatro.display.UI.TOP_RIGHT;
import static javatro.display.UI.centerText;
import static javatro.display.UI.printANSI;
import static javatro.display.UI.printBlackB;

import javatro.core.JavatroException;
import javatro.manager.options.ExitGameOption;
import javatro.manager.options.MainMenuOption;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Screen displayed upon game loss, featuring humorous defeat messages.
 *
 * <p>Contains:
 *
 * <ul>
 *   <li>Randomized loss quotes
 *   <li>Decorative border elements
 *   <li>Post-game navigation options
 * </ul>
 */
public class LoseScreen extends Screen {

    /** Collection of self-deprecating loss messages */
    private static final List<String> QUOTES =
            List.of(
                    "Maybe Go Fish is more our speed...",
                    "We folded like a cheap suit!",
                    "Time for us to shuffle off and try again!",
                    "You know what they say, the house always wins!",
                    "Looks like we found out who the real Joker is!",
                    "Oh no, were you bluffing too?",
                    "Looks like the joke's on us!",
                    "If I had hands I would have covered my eyes!",
                    "I'm literally a fool, what's your excuse?",
                    "What a flop!");

    /** Number of 'HA' repetitions in defeat banner */
    private static final int HA_REPETITIONS = 47;

    /**
     * Constructs game over screen with post-loss options.
     *
     * @throws JavatroException if screen initialization fails
     */
    public LoseScreen() throws JavatroException {
        super("\\" + RED + " GAME OVER! " + END + BLACK_B + "/");
        commandMap.add(new MainMenuOption());
        commandMap.add(new ExitGameOption());
    }

    /**
     * Displays loss screen components:
     *
     * <ul>
     *   <li>Defeat ASCII art
     *   <li>Decorative borders
     *   <li>Randomized loss message
     *   <li>Repeated "HA" defeat banner
     * </ul>
     */
    @Override
    public void displayScreen() {
        printANSI("jimbo.txt");

        int quoteIndex = ThreadLocalRandom.current().nextInt(QUOTES.size());
        assert quoteIndex >= 0 && quoteIndex < QUOTES.size() : "Invalid quote index";
        String randomQuote = QUOTES.get(quoteIndex);

        // Render top border
        printBlackB(TOP_LEFT + String.valueOf(HORIZONTAL).repeat(BORDER_WIDTH - 2) + TOP_RIGHT);
        System.out.println();

        // Display defeat message
        System.out.println(
                centerText(RED + BOLD + "HA".repeat(HA_REPETITIONS) + END + BLACK_B, BORDER_WIDTH));
        System.out.println(centerText(ITALICS + randomQuote, BORDER_WIDTH));

        // Render bottom border
        printBlackB(
                BOTTOM_LEFT + String.valueOf(HORIZONTAL).repeat(BORDER_WIDTH - 2) + BOTTOM_RIGHT);
        System.out.println();
    }
}
