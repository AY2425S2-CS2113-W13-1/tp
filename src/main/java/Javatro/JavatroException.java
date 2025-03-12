package Javatro;

/**
 * Represents a custom exception class for handling domain-specific errors in the Javatro
 * application.
 *
 * <p>This class extends {@code Exception} and provides static factory methods to create exceptions
 * for different error scenarios.
 */
public class JavatroException extends Exception {
    /**
     * Constructs an {@code JavatroException} with a specified error message.
     *
     * @param message The detailed error message explaining the exception.
     */
    public JavatroException(String message) {
        super(message);
    }

    /**
     * Creates an exception indicating an invalid number of card played.
     *
     * <p>This exception is thrown when the user plays an invalid amount of cards.
     *
     * @return An {@code JavatroException} indicating the correct number of cards to play.
     */
    public static JavatroException invalidPlayedHand() {
        return new JavatroException("A poker hand must contain between 1 and 5 cards.");
    }
}
