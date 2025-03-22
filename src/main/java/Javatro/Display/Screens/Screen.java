package Javatro.Display.Screens;

import Javatro.Core.JavatroException;
import Javatro.Manager.Options.Option;

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

    /** Fixed width for the options menu display. */
    private static final int MENU_WIDTH = 30;

    private final char BORDER_CHAR = '#'; // Custom border character

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
        this.optionsTitle = optionsTitle.trim();
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
        String border = String.valueOf(BORDER_CHAR).repeat(MENU_WIDTH);
        String paddedTitle = centerText(optionsTitle);

        System.out.println(border);
        System.out.println(paddedTitle);
        System.out.println(border);

        for (int i = 0; i < commandMap.size(); i++) {
            System.out.printf(
                    "%c  [%d] %s%n", BORDER_CHAR, i + 1, commandMap.get(i).getDescription());
        }

        System.out.println(border);
    }

    /**
     * Centers the given text within a specified width, padding it with spaces on both sides.
     *
     * @param text the text to center
     * @return the centered text surrounded by borders
     */
    private String centerText(String text) {
        int paddingSize = (Screen.MENU_WIDTH - text.length() - 2) / 2;
        return String.format(
                "%c%"
                        + paddingSize
                        + "s%s%"
                        + (Screen.MENU_WIDTH - text.length() - paddingSize - 2)
                        + "s%c",
                BORDER_CHAR,
                "",
                text,
                "",
                BORDER_CHAR);
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
