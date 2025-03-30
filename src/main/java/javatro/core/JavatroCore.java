/**
 * The {@code JavatroCore} class represents the main game model, responsible for managing game
 * rounds and initializing the game state.
 */
package javatro.core;

import javatro.core.Deck.DeckType;
import javatro.core.jokers.HeldJokers;

/** The core game logic class that manages the game state and rounds. */
public class JavatroCore {

    /** The current active round in the game. */
    public static Round currentRound;

    /** The current ante for the game. */
    protected static Ante ante;

    /** The current round count of the game. */
    protected static int roundCount;

    /** The number of plays give per round (Default value = 4). */
    public static int totalPlays;

    /** The deck used throughout the game. (A copy of this deck is made for every new Round) */
    public static Deck deck;

    /** The deck used throughout the game. (A copy of this deck is made for every new Round) */
    public static HeldJokers heldJokers;

    // @author swethaiscool
    /**
     * Retrieves the current ante.
     *
     * @return the current {@link Ante} instance
     */
    public static Ante getAnte() {
        return ante;
    }

    /**
     * Retrieves the current round count.
     *
     * @return the current round count
     */
    public static int getRoundCount() {
        return roundCount;
    }

    /** Advances the game to the next round, updating the ante and incrementing the round count. */
    public void nextRound() {
        ante.nextRound();
        roundCount++;
        startNewRound(classicRound());
    }

    /** Initializes a new game by resetting the ante, round count, jokers and decks. */
    public void setupNewGame(DeckType deckType) {
        ante = new Ante();
        roundCount = 1;
        totalPlays = 4;
        heldJokers = new HeldJokers();
        deck = new Deck(deckType);
    }
    // @author swethaiscool

    /**
     * Starts a new round and assigns it to the current round.
     *
     * @param round The new round to start.
     * @throws JavatroException If an error occurs while initializing the round.
     */
    private static void startNewRound(Round round) {
        currentRound = round;
        assert currentRound != null;
    }
    /**
     * Creates a new classic round with predefined settings.
     *
     * @return A {@code Round} instance configured as a classic round.
     * @throws JavatroException If an error occurs while creating the round.
     */
    private static Round classicRound() {
        Deck d;
        try {
            d = new Deck(deck);
            d.shuffle();
            return new Round(
                    ante.getRoundScore(),
                    4,
                    d,
                    heldJokers,
                    ante.getBlind().getName(),
                    ante.getBlind().getName());
        } catch (JavatroException javatroException) {
            System.out.println(javatroException.getMessage());
        }
        return null;
    }

    /**
     * Starts the game by initializing a new set of game parameters. This method is called when the
     * game begins.
     *
     * @throws JavatroException If an error occurs while starting the game.
     */
    public void beginGame() throws JavatroException {
        startNewRound(classicRound());
    }
}
