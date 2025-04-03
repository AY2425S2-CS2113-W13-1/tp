// @@author Markneoneo
package javatro.display.screens;

import static javatro.display.UI.BLACK_B;
import static javatro.display.UI.BLUE;
import static javatro.display.UI.BOLD;
import static javatro.display.UI.BORDER_WIDTH;
import static javatro.display.UI.BOTTOM_LEFT;
import static javatro.display.UI.BOTTOM_RIGHT;
import static javatro.display.UI.END;
import static javatro.display.UI.GREEN;
import static javatro.display.UI.HORIZONTAL;
import static javatro.display.UI.ITALICS;
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
 * Victory screen displayed upon completing the game successfully.
 *
 * <p>Features:
 *
 * <ul>
 *   <li>Random celebratory quote
 *   <li>Animated victory graphic
 *   <li>Post-game navigation options
 * </ul>
 */
public class WinGameScreen extends Screen {

    /** Collection of victory messages for random selection */
    private static final List<String> QUOTES =
            List.of(
                    "You Aced it!",
                    "You dealt with that pretty well!",
                    "Looks like you weren't bluffing!",
                    "Too bad these chips are all virtual...",
                    "Looks like I've taught you well!",
                    "You made some heads up plays!",
                    "Good thing I didn't bet against you!");

    /**
     * Constructs victory screen with post-game options.
     *
     * @throws JavatroException if screen initialization fails
     */
    public WinGameScreen() throws JavatroException {
        super("\\" + GREEN + " YOU WIN! " + END + BLACK_B + "/");
        commandMap.add(new MainMenuOption());
        commandMap.add(new ExitGameOption());
    }

    /**
     * Displays victory screen components:
     *
     * <ul>
     *   <li>Celebratory ASCII art
     *   <li>Decorative border elements
     *   <li>Random victory quote
     *   <li>Post-game options
     * </ul>
     */
    @Override
    public void displayScreen() {
        // Display primary victory graphic
        printANSI("jimbo.txt");

        // Select and display random quote
        int quoteIndex = ThreadLocalRandom.current().nextInt(QUOTES.size());
        String randomQuote = QUOTES.get(quoteIndex);
        assert quoteIndex >= 0 && quoteIndex < QUOTES.size() : "Invalid quote index";

        // Render top border
        printBlackB(TOP_LEFT + String.valueOf(HORIZONTAL).repeat(BORDER_WIDTH - 2) + TOP_RIGHT);
        System.out.println();

        // Display centered content
        System.out.println(
                centerText(
                        BLUE + BOLD + "Thanks for playing our game!" + END + BLACK_B,
                        BORDER_WIDTH));
        System.out.println(centerText(ITALICS + randomQuote, BORDER_WIDTH));

        // Render bottom border
        printBlackB(
                BOTTOM_LEFT + String.valueOf(HORIZONTAL).repeat(BORDER_WIDTH - 2) + BOTTOM_RIGHT);
        System.out.println();
    }
}
