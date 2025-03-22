package Javatro.Display.Screens;

import Javatro.Core.JavatroException;
import Javatro.Manager.Options.HelpHowToPlayOption;
import Javatro.Manager.Options.HelpIntroOption;
import Javatro.Manager.Options.HelpRulesOption;
import Javatro.Manager.Options.HelpTipsOption;
import Javatro.Manager.Options.LoadStartScreenOption;

public class HelpScreen extends Screen {

    /** Constructs a {@code StartScreen} and initializes available commands. */
    public HelpScreen() throws JavatroException {
        super("HELP SCREEN");
        commandMap.add(new HelpIntroOption());
        commandMap.add(new HelpRulesOption());
        commandMap.add(new HelpHowToPlayOption());
        commandMap.add(new HelpTipsOption());
        commandMap.add(new LoadStartScreenOption());
    }

    /** Displays the start screen, including the game logo. */
    @Override
    public void displayScreen() {
        System.out.println("\n=== Welcome to Javatro ===");
        System.out.println(
                "Javatro is a text-based roguelike deck-building game inspired by Balatro.");
        System.out.println(
                "Your goal is to strategically build a deck and progress through challenges.");
        System.out.println(
                "Each turn, you'll play poker-style hands to defeat enemies and earn new cards.");
        System.out.println(
                "\nThink strategically, manage your deck wisely, and see how far you can go!");
    }
}
