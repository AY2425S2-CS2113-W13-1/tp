// @@author flyingapricot
package javatro.display.screens;

import javatro.core.JavatroException;
import javatro.manager.options.*;
import javatro.storage.Storage;

/**
 * Initial application screen displaying main menu options and game logo.
 *
 * <p>Provides functionality for:
 *
 * <ul>
 *   <li>Displaying the Javatro logo
 *   <li>Presenting core game startup options
 *   <li>Handling basic application navigation
 * </ul>
 */
public class JumpToRunScreen extends Screen {

    /**
     * Constructs the jump to run screen with default main menu options.
     *
     * @throws JavatroException if screen initialization fails
     */
    public JumpToRunScreen() throws JavatroException {
        super("Jump To A Run");
        int numberOfRuns = Storage.getStorageInstance().getNumberOfRuns();
        super.commandMap.add(new LoadRunSelectOption());
        for (int i = 1; i <= Storage.getStorageInstance().getNumberOfRuns(); i++) {
        for(int i = 1; i <= numberOfRuns;i++) {
            SelectRunNumberOption newRunOption = new SelectRunNumberOption();
            newRunOption.setRunNumber(i);
            super.commandMap.add(newRunOption);
        }

        assert commandMap.size() == 1 + numberOfRuns : "StartScreen should have exactly numberOfRuns + 1 options";
    }

    /**
     * Displays the startup screen content including:
     *
     * <ul>
     *   <li>Javatro logo loaded from external file
     *   <li>Formatted menu options (handled by superclass)
     * </ul>
     */
    @Override
    public void displayScreen() {}
}
