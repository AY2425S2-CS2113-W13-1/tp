// @@author Markneoneo
package javatro.display.screens;

import static javatro.display.UI.BLACK_B;
import static javatro.display.UI.BOLD;
import static javatro.display.UI.END;
import static javatro.display.UI.ITALICS;
import static javatro.display.UI.printBorderedContent;

import javatro.core.JavatroException;
import javatro.manager.options.Option;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class representing a display screen in the Javatro application.
 *
 * <p>Provides common functionality for:
 *
 * <ul>
 *   <li>Managing screen options/commands
 *   <li>Displaying formatted menus
 *   <li>Handling user command selection
 * </ul>
 *
 * <p>Subclasses must implement {@link #displayScreen()} to define specific screen content.
 */
public abstract class Screen {

    /** Format string for the options title decoration */
    private static final String TITLE_FORMAT = "%s::: %s :::%s";

    /** List of available commands/options for this screen */
    protected final List<Option> commandMap = new ArrayList<>();

    /** Formatted title for the options menu section */
    private final String optionsTitle;

    /**
     * Constructs a screen with specified options menu title.
     *
     * @param optionsTitle Descriptive title for the options section (1-3 words recommended)
     * @throws JavatroException if optionsTitle is null or empty
     */
    public Screen(String optionsTitle) throws JavatroException {
        if (optionsTitle == null || optionsTitle.trim().isEmpty()) {
            throw JavatroException.invalidOptionsTitle();
        }
        this.optionsTitle = String.format(TITLE_FORMAT, BOLD, optionsTitle.trim(), END);
    }

    /**
     * Displays the main content of the screen.
     *
     * <p>Must be implemented by concrete subclasses to define:
     *
     * <ul>
     *   <li>Screen-specific header/content
     *   <li>Any additional visual elements
     *   <li>Dynamic content updates
     * </ul>
     */
    public abstract void displayScreen();

    /**
     * Displays formatted options menu with title and border decoration.
     *
     * <p>Output includes:
     *
     * <ul>
     *   <li>Styled title header
     *   <li>Numbered list of options
     *   <li>Option descriptions with consistent formatting
     *   <li>Visual border elements
     * </ul>
     */
    public void displayOptions() {
        List<String> optionLines = new ArrayList<>(commandMap.size());

        // Format each option with index, description, and styling
        for (int i = 0; i < commandMap.size(); i++) {
            Option option = commandMap.get(i);
            String desc = BLACK_B + option.getDescription() + END;
            String optionLine = BLACK_B + BOLD + "[" + (i + 1) + "] " + END + ITALICS + desc + END;

            optionLines.add(optionLine);
        }

        printBorderedContent(optionsTitle, optionLines);
    }

    /**
     * Gets the current number of available options.
     *
     * @return Number of registered commands/options (always ≥ 0)
     */
    public int getOptionsSize() {
        return commandMap.size();
    }

    /**
     * Retrieves a command by its index position.
     *
     * @param index Zero-based index of the command
     * @return Selected Option object
     * @throws JavatroException if index is out of valid range
     */
    public Option getCommand(int index) throws JavatroException {
        if (index < 0 || index >= commandMap.size()) {
            throw JavatroException.indexOutOfBounds(index);
        }

        Option selected = commandMap.get(index);
        assert selected != null : "Command list should not contain null elements";
        return selected;
    }
}
