package javatro.core;

import javatro.display.UI;

/**
 * Represents a custom exception class for handling domain-specific errors in the javatro
 * application.
 *
 * <p>This class extends {@link Exception} and provides static factory methods to create exceptions
 * for different error scenarios. It is used to handle errors related to invalid game states, such
 * as invalid card plays, invalid deck configurations, and more.
 *
 * @see Exception
 */
public final class JavatroException extends Exception {

    /**
     * Constructs a {@code JavatroException} with the specified error message.
     *
     * @param message The detailed error message explaining the exception.
     */
    public JavatroException(String message) {
        super(UI.RED + message + UI.END);
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
        return new JavatroException("A poker hand must contain between 1 and 5 cards.");
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
        return new JavatroException("Number of plays per round must be greater than 0.");
    }

    /**
     * Creates an exception indicating an invalid blind score.
     *
     * <p>This exception is thrown when the user tries to start a round with an invalid blind score.
     *
     * @return A {@code JavatroException} indicating the correct blind score.
     */
    public static JavatroException invalidBlindScore() {
        return new JavatroException("Blind score must be greater than or equal to 0!");
    }

    /**
     * Creates an exception indicating an invalid deck.
     *
     * <p>This exception is thrown when the user tries to start a round with an invalid deck.
     *
     * @return A {@code JavatroException} indicating the correct deck.
     */
    public static JavatroException invalidDeck() {
        return new JavatroException("Deck cannot be null.");
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

    /**
     * Creates an exception indicating invalid card input.
     *
     * <p>This exception is thrown when the user provides invalid input for card selection.
     *
     * @return A {@code JavatroException} indicating invalid card input.
     */
    public static JavatroException invalidCardInput() {
        return new JavatroException("Invalid input! Please enter valid numbers.");
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
        return new JavatroException("You can only select up to " + maxCardsToSelect + " cards.");
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
                "Invalid input! Please enter a number between 1 and " + maxRange + ".");
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
        return new JavatroException("Invalid input! Please enter a number.");
    }

    /**
     * Creates an exception indicating an invalid options title.
     *
     * <p>This exception is thrown when the options title is null or empty.
     *
     * @return A {@code JavatroException} indicating the options title is invalid.
     */
    public static JavatroException invalidOptionsTitle() {
        return new JavatroException("Options title cannot be null or empty.");
    }

    public static JavatroException invalidOptionsSize() {
        return new JavatroException("Number of options cannot be null or empty.");
    }

    /**
     * Creates an exception indicating an invalid screen.
     *
     * <p>This exception is thrown when the screen is null.
     *
     * @return A {@code JavatroException} indicating the screen is invalid.
     */
    public static JavatroException invalidScreen() {
        return new JavatroException("Screen cannot be null.");
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
        return new JavatroException("Index is out of bounds: " + index);
    }

    /**
     * Creates an exception indicating an error loading the logo from a file.
     *
     * <p>This exception is thrown when the logo file cannot be loaded.
     *
     * @param fileName The name of the file that could not be loaded.
     * @return A {@code JavatroException} indicating the error loading the logo.
     */
    public static JavatroException errorLoadingLogo(String fileName) {
        return new JavatroException("Error loading logo from: " + fileName);
    }

    /**
     * Creates an exception indicating an invalid selection limit.
     *
     * <p>This exception is thrown when the selection limit is less than 1.
     *
     * @return A {@code JavatroException} indicating the selection limit must be a positive value.
     */
    public static JavatroException invalidSelectionLimit() {
        return new JavatroException("Selection limit must be a positive value.");
    }

    /**
     * Creates an exception indicating no remaining discards are available.
     *
     * <p>This exception is thrown when the user tries to discard cards but no discards remain.
     *
     * @return A {@code JavatroException} indicating that no discards are available.
     */
    public static JavatroException noRemainingDiscards() {
        return new JavatroException("No remaining discards available");
    }

    /**
     * Creates an exception indicating too many cards are being discarded.
     *
     * <p>This exception is thrown when the user tries to discard more cards than allowed.
     *
     * @return A {@code JavatroException} indicating too many cards are being discarded.
     */
    public static JavatroException tooManyDiscards() {
        return new JavatroException("Too many cards selected for discarding");
    }

    /**
     * Creates an exception indicating an attempt to discard zero cards.
     *
     * <p>This exception is thrown when the user tries to discard zero cards.
     *
     * @return A {@code JavatroException} indicating that zero cards cannot be discarded.
     */
    public static JavatroException cannotDiscardZeroCards() {
        return new JavatroException("Cannot discard zero cards");
    }

    /**
     * Creates an exception indicating no more Jokers can be added.
     *
     * <p>This exception is thrown when adding 1 Joker would exceed the HOLDING_LIMIT defined in
     * {@HeldJokers}.
     *
     * @return A {@code JavatroException} indicating that the Joker cannot be added.
     */
    public static JavatroException exceedsMaxJokers() {
        return new JavatroException(
                "Cannot add more Jokers, or the maximum limit will be exceeded.");
    }
}
