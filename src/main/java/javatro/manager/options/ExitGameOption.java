package javatro.manager.options;

import static javatro.display.UI.*;

import javatro.core.JavatroException;
import javatro.display.UI;
import javatro.display.screens.StartScreen;
import javatro.manager.JavatroManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

/**
 * The {@code ExitGameOption} class represents a command that terminates the game. When executed, it
 * prints a farewell message and exits the application.
 */
public class ExitGameOption implements Option {
    private static String END_SCREEN;

    /**
     * Provides a brief description of the command.
     *
     * @return A string describing the command.
     */
    @Override
    public String getDescription() {
        return "Exit Game";
    }

    // Static block to initialize the End Screen from a file
    static {
        try (InputStream inputStream =
                StartScreen.class.getResourceAsStream("/javatro/display/end_screen.txt")) {
            if (inputStream == null) {
                throw JavatroException.errorLoadingLogo("end_screen.txt");
            }
            try (Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8)) {
                END_SCREEN = scanner.useDelimiter("\\A").next(); // Read the entire file
            }
        } catch (IOException | JavatroException e) {
            END_SCREEN = "End Screen"; // Fallback in case of error
            System.err.println(JavatroException.errorLoadingLogo("end_screen.txt").getMessage());
        }
    }

    /**
     * Executes the exit game command, displaying a farewell message and terminating the
     * application.
     */
    @Override
    public void execute() throws JavatroException {
        // display the end screen from the file
        System.out.println(END_SCREEN);

        // display the farewell message with borders and ANSI formatting
        String title = "♥️ ♠️ 🃏 " + BOLD + "GOODBYE" + " 🃏 ♦️ ♣️" + END;

        String[] lines = {RED + "WE KNOW YOU WILL BE BACK SOON" + END};

        UI.printBorderedContent(title, List.of(lines));
        JavatroManager.isExitTriggered = true;
        if(!JavatroManager.runningTests) {
            System.exit(0); // Terminate the application
        }
    }
}
