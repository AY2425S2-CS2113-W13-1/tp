// @@author Markneoneo
package javatro.core;

import static javatro.display.UI.END;
import static javatro.display.UI.RED;

/**
 * Custom exception class for handling application-specific errors in the Javatro game. Provides
 * factory methods for creating consistent exceptions with colored error messages.
 */
public final class JavatroException extends Exception {

    /**
     * Constructs a JavatroException with a colored error message.
     *
     * @param message The error message to display (will be colored red)
     */
    public JavatroException(String message) {
        super(RED + message + END);
    }

    /**
     * Constructs a JavatroException with colored message and cause.
     *
     * @param message The error message to display (colored red)
     * @param cause The underlying cause of the exception
     */
    public JavatroException(String message, Throwable cause) {
        super(RED + message + END, cause);
    }

    // Region: Factory methods for specific exception types

    // @@author JQ
    /**
     * Creates exception for invalid number of cards in a poker hand.
     *
     * @param minCards Minimum allowed cards
     * @param maxCards Maximum allowed cards
     * @return Configured exception instance
     */
    public static JavatroException invalidPlayedHand(int minCards, int maxCards) {
        return new JavatroException(
                "A poker hand must contain between " + minCards + " and " + maxCards + " cards.");
    }

    /**
     * @return Exception for invalid plays per round configuration
     */
    public static JavatroException invalidPlaysPerRound() {
        return new JavatroException("Number of plays per round must be greater than 0.");
    }

    /**
     * @return Exception for invalid blind score input
     */
    public static JavatroException invalidBlindScore() {
        return new JavatroException("Blind score must be greater than or equal to 0!");
    }

    /**
     * @return Exception for null deck configuration
     */
    public static JavatroException invalidDeck() {
        return new JavatroException("Deck cannot be null.");
    }

    /**
     * Creates an exception indicating the deck is empty.
     *
     * <p>This exception is thrown when all the cards in the deck have been drawn.
     *
     * @return A {@code JavatroException} indicating the deck is empty.
     */
    public static JavatroException noCardsRemaining() {
        return new JavatroException("Deck is empty.");
    }

    /**
     * Creates an exception indicating no plays remaining.
     *
     * <p>This exception is thrown when the user tries to play cards when no plays are remaining.
     *
     * @return A {@code JavatroException} indicating that no plays are remaining.
     */
    public static JavatroException noPlaysRemaining() {
        return new JavatroException("No plays remaining.");
    }

    // @@author Markneoneo
    /**
     * @return Exception for invalid card selection input
     */
    public static JavatroException invalidCardInput() {
        return new JavatroException("Invalid input! Please enter valid numbers.");
    }

    /**
     * Creates exception for exceeding maximum card selection.
     *
     * @param maxCardsToSelect Maximum allowed cards
     * @return Configured exception instance
     */
    public static JavatroException exceedsMaxCardSelection(int maxCardsToSelect) {
        return new JavatroException("You can only select up to " + maxCardsToSelect + " cards.");
    }

    /**
     * Creates exception for invalid menu input range.
     *
     * @param maxRange Maximum valid input value
     * @return Configured exception instance
     */
    public static JavatroException invalidMenuInput(int maxRange) {
        return new JavatroException(
                "Invalid input! Please enter a number between 1 and " + maxRange + ".");
    }

    /**
     * @return Exception for non-numeric input where number expected
     */
    public static JavatroException invalidInputType() {
        return new JavatroException("Invalid input! Please enter a number.");
    }

    /**
     * @return Exception for missing or empty options title
     */
    public static JavatroException invalidOptionsTitle() {
        return new JavatroException("Options title cannot be null or empty.");
    }

    /**
     * @return Exception for invalid options list size
     */
    public static JavatroException invalidOptionsSize() {
        return new JavatroException("Number of options cannot be null or empty.");
    }

    /**
     * @return Exception for null screen reference
     */
    public static JavatroException invalidScreen() {
        return new JavatroException("Screen cannot be null.");
    }

    /**
     * Creates exception for index out of bounds.
     *
     * @param index Invalid index that was accessed
     * @return Configured exception instance
     */
    public static JavatroException indexOutOfBounds(int index) {
        return new JavatroException("Index is out of bounds: " + index);
    }

    /**
     * Creates exception for missing resource file.
     *
     * @param fileName Name of missing file
     * @return Configured exception instance
     */
    public static JavatroException errorLoadingLogo(String fileName) {
        return new JavatroException("Error loading logo from: " + fileName);
    }

    /**
     * @return Exception for invalid selection limit
     */
    public static JavatroException invalidSelectionLimit() {
        return new JavatroException("Selection limit must be a positive value.");
    }

    // @@author JQ
    /**
     * @return Exception when no discards remain
     */
    public static JavatroException noRemainingDiscards() {
        return new JavatroException("No remaining discards available");
    }

    /**
     * @return Exception for exceeding discard limit
     */
    public static JavatroException tooManyDiscards() {
        return new JavatroException("Too many cards selected for discarding");
    }

    /**
     * @return Exception for attempting zero discards
     */
    public static JavatroException cannotDiscardZeroCards() {
        return new JavatroException("Cannot discard zero cards");
    }

    /**
     * @return Exception for exceeding maximum joker limit
     */
    public static JavatroException exceedsMaxJokers() {
        return new JavatroException(
                "Cannot add more Jokers, or the maximum limit will be exceeded.");
    }
}
