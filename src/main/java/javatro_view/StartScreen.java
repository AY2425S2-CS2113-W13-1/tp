package javatro_view;

import javatro_manager.ExitGameCommand;
import javatro_manager.LoadGameScreenCommand;
import javatro_manager.LoadOptionsScreenCommand;
import javatro_manager.HelpCommand;

/**
 * The {@code StartScreen} class represents the initial menu screen of the application. It provides
 * options to start a game, access options, or exit the game.
 */
public class StartScreen extends Screen {

    /** Constructs a {@code StartScreen} and initializes available commands. */
    public StartScreen() {
        super("START MENU");
        commandMap.add(new LoadGameScreenCommand());
        commandMap.add(new LoadOptionsScreenCommand());
        commandMap.add(new HelpCommand());
        // commandMap.put("Credits", new LoadCreditsScreenCommand(new CreditsScreen()));
        commandMap.add(new ExitGameCommand());
    }

    /** Prints the logo of the game to the console. */
    private void printLogo() {
        System.out.println("================================================");
        System.out.println(" JJJ   AAAAA  V   V   AAAAA  TTTTT  RRRR   OOO  ");
        System.out.println("  J    A   A  V   V   A   A    T    R   R O   O ");
        System.out.println("  J    AAAAA  V   V   AAAAA    T    RRRR  O   O ");
        System.out.println("  J    A   A   V V    A   A    T    R  R  O   O ");
        System.out.println(" JJJ   A   A    V     A   A    T    R   R OOO  ");
        System.out.println("================================================");
    }

    /** Displays the start screen, including the game logo. */
    @Override
    public void displayScreen() {
        printLogo();
    }
}
