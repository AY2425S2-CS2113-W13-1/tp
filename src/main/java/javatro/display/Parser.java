package javatro.display;

import static javatro.display.UI.*;

import javatro.core.JavatroException;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Handles parsing and validation of user input for the javatro application.
 *
 * <p>This class is responsible for:
 *
 * <ul>
 *   <li>Processing user input for card selections
 *   <li>Handling menu navigation input
 *   <li>Validating input ranges and formats
 *   <li>Notifying observers (e.g., {@code JavatroManager}) of user input changes
 * </ul>
 */
public class Parser {

    private static final String MENU_PROMPT =
            BLUE + BOLD + "Enter Option Index (1-%d)\n" + ARROW + END;

    private static final String CARD_PROMPT =
            BLUE
                    + UI.BOLD
                    + "Enter Card Indices between 1-%d. Up to %d cards only. (e.g. 1,2,3,4,5)\n"
                    + UI.ARROW
                    + UI.END;

    /** Property change support for notifying observers of user input changes. */
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    /**
     * Registers an observer to listen for user input changes.
     *
     * @param pcl the property change listener to register
     */
    public void addPropertyChangeListener(PropertyChangeListener pcl) throws JavatroException {
        if (pcl == null) {
            throw JavatroException.invalidScreen();
        }
        support.addPropertyChangeListener(pcl);
    }

    /**
     * Handles user input for navigating the current screen and notifies observers.
     *
     * <p>This method:
     *
     * <ul>
     *   <li>Displays current screen options
     *   <li>Validates input is within the allowed range
     *   <li>Notifies registered listeners of the valid input
     *   <li>Clears the screen after successful input
     * </ul>
     *
     * @throws JavatroException if no options are available in current screen
     */
    public void getOptionInput() throws JavatroException {
        Scanner scanner = new Scanner(System.in);
        int maxRange = getCurrentScreen().getOptionsSize();

        if (maxRange <= 0) {
            throw JavatroException.invalidOptionsSize();
        }

        while (true) {
            try {
                UI.clearScreen();
                getCurrentScreen().displayOptions();
                System.out.printf(MENU_PROMPT, maxRange);

                if (!scanner.hasNextInt()) {
                    //                    scanner.nextLine(); // Clear invalid input
                    throw JavatroException.invalidInputType();
                }

                int input = scanner.nextInt();
                //                scanner.nextLine(); // Consume newline

                if (input >= 1 && input <= maxRange) {
                    support.firePropertyChange("userInput", null, input);
                    return;
                }

                throw JavatroException.invalidMenuInput(maxRange);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                scanner.nextLine(); // Clear invalid input
            }
        }
    }

    /**
     * Prompts the user to select card numbers and returns a list of selected card indices.
     *
     * @param maxCardsAvailable the maximum number of available cards (1-based)
     * @param maxCardsToSelect the maximum number of cards a user can select
     * @return an unmodifiable list of selected card indices (0-based)
     */
    public static List<Integer> getCardInput(int maxCardsAvailable, int maxCardsToSelect) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {
                // Clear screen and show header
                UI.clearScreen();
                System.out.printf(CARD_PROMPT, maxCardsAvailable, maxCardsToSelect);

                String input = scanner.nextLine().trim();

                if (input.isEmpty()) {
                    throw JavatroException.invalidCardInput();
                }

                List<Integer> userInput = parseCardInput(input, maxCardsAvailable);

                if (userInput.size() > maxCardsToSelect) {
                    throw JavatroException.exceedsMaxCardSelection(maxCardsToSelect);
                }

                return List.copyOf(userInput);
            } catch (JavatroException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Parses and validates card input string.
     *
     * @param input the comma-separated input string
     * @param maxCardsAvailable the maximum allowed card number
     * @return list of validated card indices (0-based)
     * @throws JavatroException if no valid numbers are found
     */
    private static List<Integer> parseCardInput(String input, int maxCardsAvailable)
            throws JavatroException {
        List<Integer> userInput =
                Arrays.stream(input.split(","))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .map(
                                numStr -> {
                                    try {
                                        return Integer.parseInt(numStr);
                                    } catch (NumberFormatException e) {
                                        return null;
                                    }
                                })
                        .filter(Objects::nonNull)
                        .filter(num -> num >= 1 && num <= maxCardsAvailable)
                        .map(num -> num - 1)
                        .distinct()
                        .collect(Collectors.toList());

        if (userInput.isEmpty()) {
            throw JavatroException.invalidCardInput();
        }

        return userInput;
    }
}
