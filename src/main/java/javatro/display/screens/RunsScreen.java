package javatro.display.screens;

import static javatro.display.UI.*;

import javatro.core.JavatroException;
import javatro.manager.options.*;
import javatro.storage.Storage;

import java.util.List;
import java.util.stream.Collectors;

public class RunsScreen extends Screen {

    private static final String TITLE = String.format(
                    "%s%s%-10s  %s%-15s  %s%-10s",
                    BOLD, GREEN, "ROUND", WHITE, "ANTE", BLUE_B, "DECK");

    /**
     * Constructs a screen with the specified options title.
     *
     * @throws JavatroException if the options title is null or empty
     */
    public RunsScreen() throws JavatroException {
        super("RUNS MENU");
        commandMap.add(new MainMenuOption());
        commandMap.add(new StartGameOption());
        // New Run
        // Choose From Saved Runs
    }

    @Override
    public void displayScreen() {

        List<String> formattedDisplays = Storage.getInstance().getRunData().stream()
                .flatMap(innerList -> innerList.stream())  // Flatten the inner lists
                .map(item -> {
                    // Your formatted output for each item, similar to TITLE format
                    return String.format(
                            "%s%s%-10s  %s%-15s  %s%-10s",
                            BOLD, GREEN, "ROUND", WHITE, "ANTE", BLUE_B, "DECK"
                    );
                })
                .collect(Collectors.toList());

        printBorderedContent(TITLE, formattedDisplays);

    }
}
