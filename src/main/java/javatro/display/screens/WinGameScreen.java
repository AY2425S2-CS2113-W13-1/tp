package javatro.display.screens;

import static javatro.display.UI.*;

import javatro.core.JavatroException;
import javatro.manager.options.ExitGameOption;
import javatro.manager.options.MainMenuOption;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class WinGameScreen extends Screen {

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
     * Constructs a screen with the specified options title.
     *
     * @throws JavatroException if the options title is null or empty
     */
    public WinGameScreen() throws JavatroException {
        super("\\" + GREEN + " YOU WIN! " + END + BLACK_B + "/");
        commandMap.add(new MainMenuOption());
        commandMap.add(new ExitGameOption());
    }

    /**
     * Displays the screen content. This method must be implemented by subclasses to define the
     * specific behavior and layout of the screen.
     */
    @Override
    public void displayScreen() {
        printANSI("jimbo.txt");

        String randomQuote = QUOTES.get(ThreadLocalRandom.current().nextInt(QUOTES.size()));

        // Top border
        printBlackB(TOP_LEFT + String.valueOf(HORIZONTAL).repeat(BORDER_WIDTH - 2) + TOP_RIGHT);
        System.out.println();

        System.out.println(
                centerText(
                        BLUE + BOLD + "Thanks for playing our game!" + END + BLACK_B,
                        BORDER_WIDTH));
        System.out.println(centerText(ITALICS + randomQuote, BORDER_WIDTH));

        // Bottom border
        printBlackB(
                BOTTOM_LEFT + String.valueOf(HORIZONTAL).repeat(BORDER_WIDTH - 2) + BOTTOM_RIGHT);
        System.out.println();
    }
}
