package javatro_view;

import javatro_manager.LoadStartScreenCommand;

import java.util.Arrays;
import java.util.List;

public class OptionScreen extends Screen {

    private static final List<String> options = Arrays.asList("Enable Emotes", "Main Menu");

    public OptionScreen() {
        super("OPTIONS MENU");
        commandMap.add(new LoadStartScreenCommand());
    }

    public void printSign() {
        System.out.println("WELCOME TO OPTIONS MENU");
    }

    @Override
    public void displayScreen() {
        printSign();
    }
}
