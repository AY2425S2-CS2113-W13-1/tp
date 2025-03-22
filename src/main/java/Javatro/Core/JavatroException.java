package Javatro.Core;

/**
 * Represents a custom exception class for handling domain-specific errors in the Javatro
 * application.
 *
 * <p>This class extends {@link Exception} and provides static factory methods to create exceptions
 * for different error scenarios. It is used to handle errors related to invalid game states, such
 * as invalid card plays, invalid deck configurations, and more.
 *
 * @see Exception
 */
public final class JavatroException extends Exception {

    private static final String RED = "\u001B[31m";
    private static final String END = "\u001B[0m";

    /**
     * Constructs a {@code JavatroException} with the specified error message.
     *
     * @param message The detailed error message explaining the exception.
     */
    public JavatroException(String message) {
        super(message);
    }

    /**
     * Constructs a {@code JavatroException} with the specified error message and cause.
     *
     * @param message The detailed error message explaining the exception.
     * @param cause The cause of the exception (which is saved for later retrieval by the {@link
     *     #getCause()} method).
     */
    public JavatroException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates an exception indicating an invalid number of cards played.
     *
     * <p>This exception is thrown when the user plays an invalid number of cards.
     *
     * @return A {@code JavatroException} indicating the correct number of cards to play.
     */
    public static JavatroException invalidPlayedHand() {
        return new JavatroException(RED + "A poker hand must contain between 1 and 5 cards." + END);
    }

    /**
     * Creates an exception indicating an invalid number of plays per round.
     *
     * <p>This exception is thrown when the user tries to start a round with an invalid number of
     * plays.
     *
     * @return A {@code JavatroException} indicating the correct number of plays per round.
     */
    public static JavatroException invalidPlaysPerRound() {
        return new JavatroException(
                RED + "Number of plays per round must be greater than 0." + END);
    }

    /**
     * Creates an exception indicating an invalid blind score.
     *
     * <p>This exception is thrown when the user tries to start a round with an invalid blind score.
     *
     * @return A {@code JavatroException} indicating the correct blind score.
     */
    public static JavatroException invalidBlindScore() {
        return new JavatroException(RED + "Blind score must be greater than or equal to 0." + END);
    }

    /**
     * Creates an exception indicating an invalid deck.
     *
     * <p>This exception is thrown when the user tries to start a round with an invalid deck.
     *
     * @return A {@code JavatroException} indicating the correct deck.
     */
    public static JavatroException invalidDeck() {
        return new JavatroException(RED + "Deck cannot be null." + END);
    }

    /**
     * Creates an exception indicating no plays remaining.
     *
     * <p>This exception is thrown when the user tries to play cards when no plays are remaining.
     *
     * @return A {@code JavatroException} indicating that no plays are remaining.
     */
    public static JavatroException noPlaysRemaining() {
        return new JavatroException(RED + "No plays remaining." + END);
    }

    /**
     * Creates an exception indicating invalid card input.
     *
     * <p>This exception is thrown when the user provides invalid input for card selection.
     *
     * @return A {@code JavatroException} indicating invalid card input.
     */
    public static JavatroException invalidCardInput() {
        return new JavatroException(RED + "Invalid input! Please enter valid numbers." + END);
    }

    /**
     * Creates an exception indicating that the user has exceeded the maximum allowed card
     * selection.
     *
     * <p>This exception is thrown when the user selects more cards than allowed.
     *
     * @param maxCardsToSelect the maximum number of cards allowed to be selected
     * @return A {@code JavatroException} indicating the maximum allowed card selection.
     */
    public static JavatroException exceedsMaxCardSelection(int maxCardsToSelect) {
        return new JavatroException(
                RED + "You can only select up to " + maxCardsToSelect + " cards." + END);
    }

    /**
     * Creates an exception indicating invalid menu input.
     *
     * <p>This exception is thrown when the user provides invalid input for menu navigation.
     *
     * @param maxRange the maximum valid input range
     * @return A {@code JavatroException} indicating the valid input range.
     */
    public static JavatroException invalidMenuInput(int maxRange) {
        return new JavatroException(
                RED + "Invalid input! Please enter a number between 1 and " + maxRange + "." + END);
    }

    /**
     * Creates an exception indicating invalid input type.
     *
     * <p>This exception is thrown when the user provides non-numeric input where a number is
     * expected.
     *
     * @return A {@code JavatroException} indicating that a number is required.
     */
    public static JavatroException invalidInputType() {
        return new JavatroException(RED + "Invalid input! Please enter a number." + END);
    }

    /**
     * Creates an exception indicating an invalid options title.
     *
     * <p>This exception is thrown when the options title is null or empty.
     *
     * @return A {@code JavatroException} indicating the options title is invalid.
     */
    public static JavatroException invalidOptionsTitle() {
        return new JavatroException(RED + "Options title cannot be null or empty." + END);
    }

    /**
     * Creates an exception indicating an invalid screen.
     *
     * <p>This exception is thrown when the screen is null.
     *
     * @return A {@code JavatroException} indicating the screen is invalid.
     */
    public static JavatroException invalidScreen() {
        return new JavatroException(RED + "Screen cannot be null." + END);
    }

    /**
     * Creates an exception indicating an index is out of bounds.
     *
     * <p>This exception is thrown when an invalid index is accessed.
     *
     * @param index The invalid index that caused the exception.
     * @return A {@code JavatroException} indicating the index is out of bounds.
     */
    public static JavatroException indexOutOfBounds(int index) {
        return new JavatroException(RED + "Index is out of bounds: " + index + END);
    }
}
