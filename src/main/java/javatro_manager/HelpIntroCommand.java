package javatro_manager;

import javatro_view.JavatroView;

/**
 * The HelpIntroCommand class provides an introduction to Javatro. This command is executed when the
 * player requests an introduction.
 */
public class HelpIntroCommand implements Command {

    public HelpIntroCommand() {}

    /** Executes the command to display the introduction message. */
    @Override
    public void execute() {
        System.out.println("\n=== Welcome to Javatro ===");
        System.out.println(
                "Javatro is a text-based roguelike deck-building game inspired by Balatro.");
        System.out.println(
                "Your goal is to strategically build a deck and progress through challenges.");
        System.out.println(
                "Each turn, you'll play poker-style hands to defeat enemies and earn new cards.");
        System.out.println(
                "\nThink strategically, manage your deck wisely, and see how far you can go!");

        JavatroManager.setScreen(JavatroView.getHelpScreen());
    }

    /**
     * Returns a description of this command.
     *
     * @return A string describing the command.
     */
    @Override
    public String getDescription() {
        return "Game Introduction";
    }
}
