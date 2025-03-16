package javatro_view;

import javatro_manager.ExitGameCommand;
import javatro_manager.LoadGameScreenCommand;
import javatro_manager.LoadOptionsScreenCommand;

public class StartScreen extends Screen {

    public StartScreen() {
        super("START MENU");
        commandMap.add(new LoadGameScreenCommand());
        commandMap.add(new LoadOptionsScreenCommand());
        // commandMap.put("Credits", new LoadCreditsScreenCommand(new CreditsScreen()));
        commandMap.add(new ExitGameCommand());
    }

    private void printLogo() {
        System.out.println("================================================");
        System.out.println(" JJJ   AAAAA  V   V   AAAAA  TTTTT  RRRR   OOO  ");
        System.out.println("  J    A   A  V   V   A   A    T    R   R O   O ");
        System.out.println("  J    AAAAA  V   V   AAAAA    T    RRRR  O   O ");
        System.out.println("  J    A   A   V V    A   A    T    R  R  O   O ");
        System.out.println(" JJJ   A   A    V     A   A    T    R   R OOO  ");
        System.out.println("================================================");
    }

    @Override
    public void displayScreen() {
        printLogo();
    }
}
