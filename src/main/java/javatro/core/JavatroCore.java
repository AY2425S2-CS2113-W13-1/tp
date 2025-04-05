/**
 * The {@code JavatroCore} class represents the main game model, responsible for managing game
 * rounds and initializing the game state.
 */
package javatro.core;

import javatro.core.Deck.DeckType;
import javatro.core.jokers.HeldJokers;
import javatro.core.round.Round;
import javatro.storage.Storage;

import java.util.EnumMap;
import java.util.Map;

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

    /** Stores the play counts for each poker hand type */
    private static final Map<PokerHand.HandType, Integer> pokerHandPlayCounts =
            new EnumMap<>(PokerHand.HandType.class);

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
        ante.setBlind(Storage.BlindFromKey(Storage.getStorageInstance().getValue(Storage.getStorageInstance().getRunChosen(),9)));
        ante.setAnteCount(Integer.parseInt(Storage.getStorageInstance().getValue(Storage.getStorageInstance().getRunChosen(),3)));
        roundCount = Integer.parseInt(Storage.getStorageInstance().getValue(Storage.getStorageInstance().getRunChosen(),1));
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
                    ante, 4, d, heldJokers, ante.getBlind().getName(), ante.getBlind().getName());
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

    /** Initializes poker hand play counts at game start */
    public static void initializePokerHandStats() {
        for (PokerHand.HandType handType : PokerHand.HandType.values()) {
            pokerHandPlayCounts.put(handType, 0);
        }
    }

    /** Gets the play count for a specific hand type */
    public static int getPlayCount(PokerHand.HandType handType) {
        return pokerHandPlayCounts.getOrDefault(handType, 0);
    }

    /** Increments the play count for a specific hand type */
    public static void incrementPlayCount(PokerHand.HandType handType) {
        pokerHandPlayCounts.put(handType, getPlayCount(handType) + 1);
    }
}
