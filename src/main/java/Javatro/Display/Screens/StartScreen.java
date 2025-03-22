package Javatro.Display.Screens;

import Javatro.Core.JavatroException;
import Javatro.Manager.Options.ExitGameOption;
import Javatro.Manager.Options.HelpOption;
import Javatro.Manager.Options.LoadGameScreenOption;

/**
 * The {@code StartScreen} class represents the initial menu screen of the application. It provides
 * options to start a game, access options, or exit the game.
 */
public class StartScreen extends Screen {

    /** Constructs a {@code StartScreen} and initializes available commands. */
    public StartScreen() throws JavatroException {
        super("START MENU");
        commandMap.add(new LoadGameScreenOption());
        commandMap.add(new HelpOption());
        // commandMap.put("Credits", new LoadCreditsScreenCommand(new CreditsScreen()));
        commandMap.add(new ExitGameOption());
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
