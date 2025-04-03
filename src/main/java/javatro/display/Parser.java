// @@ Markneoneo
package javatro.display;

import static javatro.display.UI.BLUE;
import static javatro.display.UI.BOLD;
import static javatro.display.UI.END;
import static javatro.display.UI.getCurrentScreen;

import javatro.core.JavatroException;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Handles parsing and validation of user input for the Javatro application.
 *
 * <p>This class is responsible for:
 *
 * <ul>
 *   <li>Processing user input for card selections and menu navigation
 *   <li>Validating input ranges, formats, and constraints
 *   <li>Notifying registered observers of valid user input changes
 * </ul>
 */
public class Parser {

    private static final String MENU_PROMPT = BLUE + BOLD + "Enter Option Index (1-%d)\n>>> " + END;

    private static final String CARD_PROMPT =
            BLUE
                    + BOLD
                    + "Enter Card Indices between 1-%d. Up to %d cards only. (e.g. 1,2,3,4,5)\n>>> "
                    + END;

    /** Property change support for notifying observers of user input changes. */
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    /**
     * Registers an observer to listen for user input changes.
     *
     * @param pcl the property change listener to register
     * @throws JavatroException if the provided listener is {@code null}
     */
    public void addPropertyChangeListener(PropertyChangeListener pcl) throws JavatroException {
        assert pcl != null : "PropertyChangeListener cannot be null";
        if (pcl == null) {
            throw JavatroException.invalidScreen();
        }
        support.addPropertyChangeListener(pcl);
    }

    /**
     * Handles user input for menu navigation and notifies observers.
     *
     * <p>This method:
     *
     * <ul>
     *   <li>Displays current screen options
     *   <li>Validates input is within allowed range
     *   <li>Notifies listeners of valid input
     *   <li>Handles input errors with appropriate error messages
     * </ul>
     *
     * @throws JavatroException if no options are available in current screen
     */
    public void getOptionInput() throws JavatroException {
        Scanner scanner = new Scanner(System.in);
        int maxRange = getCurrentScreen().getOptionsSize();
        assert maxRange > 0 : "Screen options size must be positive";

        if (maxRange <= 0) {
            throw JavatroException.invalidOptionsSize();
        }

        while (true) {
            try {
                getCurrentScreen().displayOptions();
                System.out.printf(MENU_PROMPT, maxRange);

                if (!scanner.hasNextInt()) {
                    throw JavatroException.invalidInputType();
                }

                int input = scanner.nextInt();
                assert input != 0 : "Menu input should never be 0 (options start at 1)";

                if (input >= 1 && input <= maxRange) {
                    support.firePropertyChange("userInput", null, input);
                    return;
                }

                throw JavatroException.invalidMenuInput(maxRange);
            } catch (JavatroException e) {
                System.out.println(e.getMessage());
                scanner.nextLine(); // Clear scanner buffer
            }
        }
    }

    /**
     * Collects and validates card selection input from the user.
     *
     * @param maxCardsAvailable maximum number of available cards (1-based)
     * @param maxCardsToSelect maximum number of cards allowed for selection
     * @return unmodifiable list of 0-based card indices
     */
    public static List<Integer> getCardInput(int maxCardsAvailable, int maxCardsToSelect) {
        assert maxCardsAvailable > 0 : "Must have at least one card available";
        assert maxCardsToSelect > 0 : "Must be able to select at least one card";
        assert maxCardsToSelect <= maxCardsAvailable : "Cannot select more cards than available";

        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {
                System.out.printf(CARD_PROMPT, maxCardsAvailable, maxCardsToSelect);
                String input = scanner.nextLine().trim();

                if (input.isEmpty()) {
                    throw JavatroException.invalidCardInput();
                }

                List<Integer> userInput = parseCardInput(input, maxCardsAvailable);

                assert !userInput.isEmpty() : "Parsed card input should never be empty";

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
     * Parses and validates raw card input string.
     *
     * @param input comma-separated input string
     * @param maxCardsAvailable maximum allowed card number (1-based)
     * @return validated list of 0-based card indices
     * @throws JavatroException if no valid card numbers are found
     */
    private static List<Integer> parseCardInput(String input, int maxCardsAvailable)
            throws JavatroException {
        assert input != null : "Input string cannot be null";
        assert !input.trim().isEmpty() : "Input string cannot be empty";
        assert maxCardsAvailable > 0 : "Must have at least one card available";

        List<Integer> userInput =
                Arrays.stream(input.split(","))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .map(
                                numStr -> {
                                    try {
                                        return Integer.parseInt(numStr);
                                    } catch (NumberFormatException e) {
                                        return null; // Mark invalid entries as null
                                    }
                                })
                        .filter(Objects::nonNull)
                        .filter(num -> num >= 1 && num <= maxCardsAvailable)
                        .map(num -> num - 1) // Convert to 0-based index
                        .distinct()
                        .collect(Collectors.toList());

        assert userInput.stream().allMatch(i -> i >= 0 && i < maxCardsAvailable)
                : "All card indices should be within valid range";

        if (userInput.isEmpty()) {
            throw JavatroException.invalidCardInput();
        }

        return userInput;
    }
}
