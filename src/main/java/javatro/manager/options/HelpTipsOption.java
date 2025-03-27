//@@author swethacool
package javatro.manager.options;

import static javatro.display.UI.BOLD;
import static javatro.display.UI.END;

import javatro.core.JavatroException;
import javatro.display.UI;
import javatro.manager.JavatroManager;

import java.util.List;

/**
 * The HelpTipsOption class provides gameplay tips for javatro. This command is executed when the
 * player requests tips for improving their strategy.
 */
public class HelpTipsOption implements Option {

    /**
     * Returns a description of this command.
     *
     * @return A string describing the command.
     */
    @Override
    public String getDescription() {
        return "Tips and Tricks";
    }

    /** Executes the command to display gameplay tips. */
    @Override
    public void execute() throws JavatroException {
        String title = "♥️ ♠️ 🃏 " + BOLD + "Pro Tips For Javatro" + " 🃏 ♦️ ♣️" + END;

        String[] lines = {
            "- Focus on building a balanced deck with attack and defense cards.",
            "- Don't just add every card you find—some cards can weaken your deck.",
            "- Save strong hands for tougher enemies.",
            "- Adapt your strategy based on the enemies you encounter.",
            "- Experiment with different playstyles to find what works best for you.",
            "",
            "Good luck and have fun!"
        };

        UI.printBorderedContent(title, List.of(lines));
        JavatroManager.setScreen(UI.getHelpScreen());
    }
}
