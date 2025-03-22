package Javatro.Manager;

import static Javatro.Display.UI.getCurrentScreen;

import Javatro.Core.JavatroException;
import Javatro.Display.UI;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Handles parsing and validation of user input for the Javatro application.
 *
 * <p>This class is responsible for processing user input, such as card selections and menu
 * navigation, and notifying observers (e.g., {@code JavatroManager}) of changes in user input.
 */
public class Parser {

    /** Property change support for notifying observers of user input changes. */
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    /**
     * Registers an observer to listen for user input changes.
     *
     * @param pcl the property change listener to register
     */
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    /**
     * Prompts the user to select card numbers and returns a list of selected card indices.
     *
     * @param maxCardsAvailable the maximum number of available cards
     * @param maxCardsToSelect the maximum number of cards a user can select
     * @return a list of selected card indices
     * @throws JavatroException if the input is invalid or exceeds the allowed number of cards
     */
    public static List<Integer> getCardInput(int maxCardsAvailable, int maxCardsToSelect)
            throws JavatroException {
        Scanner scanner = new Scanner(System.in);
        List<Integer> userInput;

        while (true) {
            System.out.println(
                    "Enter numbers (comma-separated, e.g., 1,2,3) from 1 to "
                            + maxCardsAvailable
                            + " (Allowed to select only "
                            + maxCardsToSelect
                            + "):");

            String input = scanner.nextLine().trim();
            String[] inputArray = input.split(",");

            userInput =
                    Arrays.stream(inputArray)
                            .map(String::trim)
                            .map(
                                    numStr -> {
                                        try {
                                            return Integer.parseInt(numStr) - 1;
                                        } catch (NumberFormatException e) {
                                            return null;
                                        }
                                    })
                            .filter(num -> num != null && num >= 0 && num < maxCardsAvailable)
                            .collect(Collectors.toList());

            if (userInput.isEmpty()) {
                throw JavatroException.invalidCardInput();
            }

            if (userInput.size() > maxCardsToSelect) {
                throw JavatroException.exceedsMaxCardSelection(maxCardsToSelect);
            }

            System.out.println("You selected the numbers: " + userInput);
            break;
        }
        return userInput;
    }

    /** Handles user input for navigating the current screen and notifies observers. */
    public void getInput() {
        Scanner scanner = new Scanner(System.in);
        int userInput;
        int maxRange = getCurrentScreen().getOptionsSize();

        while (true) {
            try {
                getCurrentScreen().displayOptions();
                System.out.print("Enter a number (1 to " + maxRange + "): ");

                if (scanner.hasNextInt()) {
                    userInput = scanner.nextInt();
                    if (userInput >= 1 && userInput <= maxRange) {
                        break; // Exit the loop if input is valid
                    } else {
                        throw JavatroException.invalidMenuInput(maxRange);
                    }
                } else {
                    throw JavatroException.invalidInputType();
                }
            } catch (JavatroException e) {
                System.err.println(e.getMessage()); // Print the error message
                scanner.nextLine(); // Clear the invalid input from the scanner
            } catch (Exception e) {
                System.err.println("An unexpected error occurred: " + e.getMessage());
                scanner.nextLine(); // Clear the invalid input from the scanner
            }
        }

        // Notify listeners (e.g., JavatroManager) of the updated user input
        support.firePropertyChange("userInput", null, userInput);
        UI.clearScreen();
    }
}
