package javatro.display.screens;

import static javatro.display.UI.*;

import javatro.core.JavatroException;
import javatro.manager.options.*;
import javatro.storage.Storage;

import java.util.List;
import java.util.stream.Collectors;

public class RunsScreen extends Screen {

    private static final String TITLE =
            String.format(
                    "%s%s%-10s  %s%-10s  %s%-10s%s",
                    BOLD, GREEN, "ROUND", WHITE, "ANTE", BLUE_B, "DECK", END);

    /**
     * Constructs a screen with the specified options title.
     *
     * @throws JavatroException if the options title is null or empty
     */
    public RunsScreen() throws JavatroException {
        super("RUNS MENU");
        commandMap.add(new MainMenuOption());
        for (int run = 0; run < Storage.getInstance().getRunData().size(); run++) {
            ChooseRunOption chooseRunOption = new ChooseRunOption();
            chooseRunOption.setRunNumber(run);
            commandMap.add(chooseRunOption);
        }
    }

    public void runOverview() {
        List<String> formattedDisplays =
                Storage.getInstance().getRunData().stream()
                        .filter(innerList -> innerList.size() >= 3)
                        .map(
                                innerList ->
                                        String.format(
                                                "%s%-10s  %s%-10s  %s%-10s%s",
                                                GREEN,
                                                innerList.get(0), // ROUND value
                                                WHITE,
                                                innerList.get(1), // ANTE value
                                                BLUE_B,
                                                innerList.get(2), // DECK value
                                                END))
                        .collect(Collectors.toList());

        printBorderedContent(TITLE, formattedDisplays);
    }

    @Override
    public void displayScreen() {
        runOverview();
    }
}
