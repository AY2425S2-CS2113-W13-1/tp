package javatro.display.screens;

import javatro.core.JavatroException;
import javatro.display.UI;
import javatro.manager.options.Option;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@code Screen} class serves as an abstract base class for all screens in the application. It
 * defines common behaviors such as displaying options for the user to select and handling commands.
 * Subclasses must implement the {@link #displayScreen()} method to define their specific content.
 */
public abstract class Screen {

    /** A list of commands associated with this screen. */
    protected final List<Option> commandMap = new ArrayList<>();

    /** The title of the options menu displayed on the screen. */
    private final String optionsTitle;

    /**
     * Constructs a screen with the specified options title.
     *
     * @param optionsTitle the title of the options menu (cannot be null or empty)
     * @throws JavatroException if the options title is null or empty
     */
    public Screen(String optionsTitle) throws JavatroException {
        if (optionsTitle == null || optionsTitle.trim().isEmpty()) {
            throw JavatroException.invalidOptionsTitle();
        }
        this.optionsTitle = String.format("%s::: %s :::%s", UI.BOLD, optionsTitle.trim(), UI.END);
    }

    /**
     * Displays the screen content. This method must be implemented by subclasses to define the
     * specific behavior and layout of the screen.
     */
    public abstract void displayScreen();

    /**
     * Displays the available options in a formatted menu style. The menu includes a border, a
     * centered title, and a list of options with descriptions.
     */
    public void displayOptions() {
        List<String> optionLines = new ArrayList<>();

        for (int i = 0; i < commandMap.size(); i++) {
            String desc = UI.BLACK_B + commandMap.get(i).getDescription() + UI.END;
            String option =
                    UI.BLACK_B
                            + UI.BOLD
                            + "["
                            + (i + 1)
                            + "] "
                            + UI.END
                            + UI.ITALICS
                            + desc
                            + UI.END;

            optionLines.add(option);
        }

        UI.printBorderedContent(optionsTitle, optionLines);
    }

    /**
     * Returns the number of available options (commands) in this screen.
     *
     * @return the number of options available
     */
    public int getOptionsSize() {
        return commandMap.size();
    }

    /**
     * Retrieves the command associated with the given index.
     *
     * @param index the index of the command (0-based)
     * @return the command at the specified index
     * @throws JavatroException if the index is out of bounds
     */
    public Option getCommand(int index) throws JavatroException {
        if (index < 0 || index >= commandMap.size()) {
            throw JavatroException.indexOutOfBounds(index);
        }
        return commandMap.get(index);
    }
}
