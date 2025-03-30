// @@author swethacool
package javatro.display.screens;

import javatro.core.JavatroException;
import javatro.manager.options.HelpHowOption;
import javatro.manager.options.HelpIntroOption;
import javatro.manager.options.HelpRulesOption;
import javatro.manager.options.HelpTipsOption;
import javatro.manager.options.MainMenuOption;
import javatro.manager.options.PokerHandOption;

/**
 * The {@code HelpScreen} class represents a screen displaying the help menu in the javatro
 * application. It provides various help options, including an introduction, rules, how-to-play
 * guide, and tips. Users can also return to the main menu from this screen.
 */
public class HelpScreen extends Screen {

    /**
     * Constructs a {@code HelpScreen} instance and initializes the available help options.
     *
     * @throws JavatroException if an error occurs while setting up the screen
     */
    public HelpScreen() throws JavatroException {
        super("HELP MENU");
        commandMap.add(new HelpIntroOption());
        commandMap.add(new HelpRulesOption());
        commandMap.add(new HelpHowOption());
        commandMap.add(new HelpTipsOption());
        commandMap.add(new PokerHandOption());
        commandMap.add(new MainMenuOption());
    }

    /**
     * Displays the help screen. This method should be overridden to define how the help menu is
     * presented.
     */
    @Override
    public void displayScreen() {}
}
