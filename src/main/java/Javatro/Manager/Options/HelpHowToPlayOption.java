package Javatro.Manager.Options;

import Javatro.Core.JavatroException;
import Javatro.Display.UI;
import Javatro.Manager.JavatroManager;

public class HelpHowToPlayOption implements Option {

    /**
     * The HelpHowToPlayOption class explains how to play Javatro. This command is executed when the
     * player requests gameplay instructions.
     */
    public HelpHowToPlayOption() {}

    /** Executes the command to display instructions on how to play. */
    @Override
    public void execute() throws JavatroException {
        System.out.println("\n=== How to Play Javatro ===");
        System.out.println("1. Start a new game with 'start'.");
        System.out.println("2. Draw cards using 'draw'.");
        System.out.println("3. Play poker hands to attack enemies.");
        System.out.println("4. Earn new cards and modify your deck.");
        System.out.println("5. Survive as long as possible and improve your strategy.");
        System.out.println("\nUse commands like:");
        System.out.println("   - 'attack'  : Play a hand to deal damage.");
        System.out.println("   - 'defend'  : Use a card to reduce incoming damage.");
        System.out.println("   - 'exit'    : Quit the game.");

        JavatroManager.setScreen(UI.getHelpScreen());
    }

    /**
     * Returns a description of this command.
     *
     * @return A string describing the command.
     */
    @Override
    public String getDescription() {
        return "How To Play";
    }
}
