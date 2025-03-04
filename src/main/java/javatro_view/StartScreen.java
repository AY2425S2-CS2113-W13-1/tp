package javatro_view;

import java.util.Arrays;
import java.util.List;

public class StartScreen extends Screen {

    private static final List<String> options =
            Arrays.asList("Start Game", "Options", "Credits", "Exit");

    public StartScreen() {
        super(options);
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
        super.displayOptions();
    }
}
