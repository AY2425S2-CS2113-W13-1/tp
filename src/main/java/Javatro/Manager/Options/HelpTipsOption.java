package Javatro.Manager.Options;

import Javatro.Core.JavatroException;
import Javatro.Display.UI;
import Javatro.Manager.JavatroManager;

public class HelpTipsOption implements Option {

    /**
     * The HelpTipsOption class provides gameplay tips for Javatro. This command is executed when
     * the player requests tips for improving their strategy.
     */
    public HelpTipsOption() {}

    /** Executes the command to display gameplay tips. */
    @Override
    public void execute() throws JavatroException {
        System.out.println("\n=== Pro Tips for Javatro ===");
        System.out.println("- Focus on building a balanced deck with attack and defense cards.");
        System.out.println("- Don't just add every card you find—some cards can weaken your deck.");
        System.out.println("- Save strong hands for tougher enemies.");
        System.out.println("- Adapt your strategy based on the enemies you encounter.");
        System.out.println(
                "- Experiment with different playstyles to find what works best for you.");
        System.out.println("\nGood luck and have fun!");

        JavatroManager.setScreen(UI.getHelpScreen());
    }

    /**
     * Returns a description of this command.
     *
     * @return A string describing the command.
     */
    @Override
    public String getDescription() {
        return "Tips and Tricks";
    }
}
