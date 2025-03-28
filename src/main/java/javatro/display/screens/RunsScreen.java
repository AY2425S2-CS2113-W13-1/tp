package javatro.display.screens;

import javatro.core.JavatroException;
import javatro.manager.options.*;

import static javatro.display.UI.*;

public class RunsScreen extends Screen{

    private static final String TITLE =
            String.format(
                    "%s%s%-5s     %s%-15s       %s%-5s%s × %s%-5s%s     %s%-6s%s",
                    BOLD,
                    GREEN,
                    "ROUND",
                    WHITE,
                    "ANTE",
                    BLUE_B,
                    "DECK");


    /**
     * Constructs a screen with the specified options title.
     *
     * @throws JavatroException if the options title is null or empty
     */
    public RunsScreen() throws JavatroException {
        super("RUNS MENU");
        commandMap.add(new MainMenuOption());
        commandMap.add(new StartGameOption());
        //New Run
        //Choose From Saved Runs
    }

    @Override
    public void displayScreen() {
        System.out.println("New Run");
    }
}
