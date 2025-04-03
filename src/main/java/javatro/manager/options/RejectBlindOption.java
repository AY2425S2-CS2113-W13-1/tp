package javatro.manager.options;

import javatro.core.Ante;
import javatro.core.JavatroCore;
import javatro.core.JavatroException;
import javatro.display.UI;
import javatro.manager.JavatroManager;

import java.util.List;

// @@author swethacool
/**
 * Represents an option to reject the current blind and move to the next available blind level.
 * Implements the {@code Option} interface.
 */
public class RejectBlindOption implements Option {

    /**
     * Returns the description of this option.
     *
     * @return A string representing the description of the option.
     */
    @Override
    public String getDescription() {
        return "Reject Blind";
    }

    /**
     * Executes the action of rejecting the current blind level. If the current blind is {@code
     * SMALL_BLIND}, it moves to {@code LARGE_BLIND}. If the current blind is {@code LARGE_BLIND},
     * it moves to {@code BOSS_BLIND}. If already at {@code BOSS_BLIND}, a warning message is
     * displayed, indicating that the player must play at this level.
     *
     * @throws JavatroException if an error occurs during execution.
     */
    @Override
    public void execute() throws JavatroException {
        if (JavatroCore.getAnte().getBlind() == Ante.Blind.SMALL_BLIND) {
            JavatroCore.getAnte().setBlind(Ante.Blind.LARGE_BLIND);
            JavatroManager.setScreen(UI.getBlindScreen());
        } else if (JavatroCore.getAnte().getBlind() == Ante.Blind.LARGE_BLIND) {
            JavatroCore.getAnte().setBlind(Ante.Blind.BOSS_BLIND);
            JavatroManager.setScreen(UI.getBlindScreen());
        } else {
            String title = "⚠\uFE0F " + UI.BOLD + "WARNING" + " ⚠\uFE0F" + UI.END;

            String[] lines = {
                "Oops! you have to play the BOSS blind to proceed, no skipping! :D",
            };

            UI.printBorderedContent(title, List.of(lines));
            JavatroManager.setScreen(UI.getBlindScreen());
        }
    }
}
