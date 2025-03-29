/**
 * The {@code JavatroCore} class represents the main game model, responsible for managing game
 * rounds and initializing the game state.
 */
package javatro.core;

/** The core game logic class that manages the game state and rounds. */
public class JavatroCore {

    /** The current active round in the game. */
    public static Round currentRound;

    /**
     * Starts a new round and assigns it to the current round.
     *
     * @param round The new round to start.
     */
    private void startNewRound(Round round) {
        currentRound = round;
    }
    /**
     * Creates a new classic round with predefined settings.
     *
     * @return A {@code Round} instance configured as a classic round.
     */
    private Round classicRound() {
        Deck d = new Deck();
        try {
            return new Round(300, 4, d, "Classic", "Classic Round");
        } catch (JavatroException javatroException) {
            System.out.println(javatroException.getMessage());
        }
        return null;
    }

    /** Starts the game by initializing a new round. This method is called when the game begins. */
    public void beginGame() {
        startNewRound(classicRound());
    }

    // A method to initialize the deck from a string value
    //    private void Deck createDeckFromString(String deckTypeString) {
    //        DeckType deckType;
    //        try {
    //            // Convert the string to the corresponding DeckType enum
    //            deckType = DeckType.valueOf(deckTypeString);  // Convert to uppercase to handle
    // case insensitivity
    //        } catch (IllegalArgumentException e) {
    //            // Handle the case where the string doesn't match a valid DeckType
    //            deckType = DeckType.DEFAULT;  // Defaulting to "DEFAULT" Decktype
    //        }
    //
    //        // Return a new Deck initialized with the valid DeckType
    //        return new Deck(deckType);
    //    }

}
