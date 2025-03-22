package Javatro.Manager.Options;

import Javatro.Core.JavatroException;
import Javatro.Display.UI;
import Javatro.Manager.JavatroManager;

/**
 * The HelpOption class handles displaying the help screen in Javatro. This command is executed when
 * the player selects the help option.
 */
public class HelpOption implements Option {

    public HelpOption() {}

    /** Executes the command to display the help screen. */
    @Override
    public void execute() throws JavatroException {
        JavatroManager.setScreen(UI.getHelpScreen());
    }

    /**
     * Returns a description of this command.
     *
     * @return A string describing the command.
     */
    @Override
    public String getDescription() {
        return "Select Help";
    }
}
