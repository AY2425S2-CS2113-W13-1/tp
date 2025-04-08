/**
 * The {@code JavatroCore} class represents the main game model, responsible for managing game
 * rounds and initializing the game state.
 */
package javatro.core;

import javatro.core.Deck.DeckType;
import javatro.core.jokers.HeldJokers;
import javatro.core.round.Round;
import javatro.storage.DataParser;
import javatro.storage.Storage;
import javatro.storage.StorageManager;
import javatro.storage.utils.CardUtils;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/** The core game logic class that manages the game state and rounds. */
public class JavatroCore {

    // 1. Constants (Static Final)
    /** The Storage instance used for saving and retrieving data. */
    private static final Storage storage = Storage.getStorageInstance();

    /** Stores the play counts for each poker hand type */
    private static final Map<PokerHand.HandType, Integer> pokerHandPlayCounts =
            new EnumMap<>(PokerHand.HandType.class);

    /** The current round count of the game. */
    protected static int roundCount;

    // 3. Static Protected Variables (Protected APIs)
    /** The current ante for the game. */
    protected static Ante ante;

    // 2. Static Public Variables (Public APIs)
    /** The current active round in the game. */
    public static Round currentRound;

    /** The deck used throughout the game. (A copy of this deck is made for every new Round) */
    public static Deck deck;

    /** The held jokers used throughout the game. */
    public static HeldJokers heldJokers;

    /** The number of plays given per round (Default value = 4). */
    public static int totalPlays;

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
        Round nextRound = classicRound();

        // Prepare all the data to be saved
        int runIndex = storage.getRunChosen() - 1;

        List<String> roundData = new ArrayList<>();
        roundData.add(String.valueOf(roundCount)); // Update Round Count
        roundData.add(String.valueOf(ante.getAnteCount())); // Update Ante Count
        roundData.add(ante.getBlind().getName()); // Update Blind

        // Update Holding Hand
        for (int i = 0; i < 8; i++) {
            assert nextRound != null;
            Card currentCard = nextRound.getPlayerHandCards().get(i);
            roundData.add(CardUtils.cardToString(currentCard)); // Update Holding Hand Cards
        }

        // Delegate the update to StorageManager
        StorageManager.getInstance().updateRoundData(runIndex, roundData);

        // Update deck
        deck = new Deck(CardUtils.deckFromKey(storage.getValue(runIndex, DataParser.DECK_INDEX)));
        Storage.isNewDeck = true;

        startNewRound(nextRound);
    }

    /**
     * Initializes a new game by resetting the ante, round count, jokers and decks.
     *
     * @param deckType The type of deck to be used for the new game.
     */
    public void setupNewGame(DeckType deckType) {
        ante = new Ante();

        // Fetch all relevant data for the current run in one go
        int runIndex = storage.getRunChosen() - 1;
        ArrayList<String> runData = StorageManager.getInstance().getRunData(runIndex);

        assert runData != null : "Run data should not be null";
        assert runData.size() > DataParser.JOKER_HAND_START_INDEX + 5
                : "Run data is incomplete or corrupted";

        // Initialize ante and round count
        ante.setBlind(CardUtils.blindFromKey(runData.get(DataParser.BLIND_INDEX)));
        ante.setAnteCount(Integer.parseInt(runData.get(DataParser.ANTE_NUMBER_INDEX)));
        roundCount = Integer.parseInt(runData.get(DataParser.ROUND_NUMBER_INDEX));

        totalPlays = 4;

        // Update Jokers
        heldJokers = new HeldJokers();
        for (int i = DataParser.JOKER_HAND_START_INDEX;
                i < DataParser.JOKER_HAND_START_INDEX + 5;
                i++) {
            String jokerString = runData.get(i);
            if (jokerString.equals("-") || jokerString.equals("NA")) {
                continue;
            }

            try {
                heldJokers.add(CardUtils.parseJokerString(jokerString));
            } catch (JavatroException e) {
                throw new RuntimeException(e);
            }
        }

        deck = new Deck(deckType);
    }

    /**
     * Starts a new round and assigns it to the current round.
     *
     * @param round The new round to start.
     */
    private static void startNewRound(Round round) {
        currentRound = round;
        // Set round number, discards and hands
        assert currentRound != null;

        int savedPlays =
                Integer.parseInt(
                        storage.getValue(storage.getRunChosen() - 1, DataParser.HAND_INDEX));
        int savedDiscards =
                Integer.parseInt(
                        storage.getValue(storage.getRunChosen() - 1, DataParser.DISCARD_INDEX));

        if (savedPlays == -1) {
            savedPlays = 4;
        }
        if (savedDiscards == -1) {
            savedDiscards = 3;
        }

        currentRound.updatePlays(
                deck.getDeckName().getName().equals("BLUE") && savedPlays == 4
                        ? savedPlays + 1
                        : savedPlays);
        currentRound.updateDiscards(
                deck.getDeckName().getName().equals("RED") && savedDiscards == 3
                        ? savedDiscards + 1
                        : savedDiscards);

        // Collect all the data that needs to be updated
        int runIndex = storage.getRunChosen() - 1;
        List<String> roundData = new ArrayList<>();
        roundData.add(String.valueOf(currentRound.getRemainingPlays())); // HAND_INDEX
        roundData.add(String.valueOf(currentRound.getRemainingDiscards())); // DISCARD_INDEX
        roundData.add(String.valueOf(currentRound.getCurrentScore())); // ROUND_SCORE_INDEX

        // Update StorageManager in one go
        StorageManager.getInstance().updateNewRoundData(runIndex, roundData);

        try {
            storage.updateSaveFile();
        } catch (JavatroException e) {
            System.out.println("Could not save info.");
        }
    }

    /**
     * Creates a new classic round with predefined settings.
     *
     * @return A {@code Round} instance configured as a classic round.
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
     */
    public void beginGame() {
        Round newRound = Objects.requireNonNull(classicRound());

        // Update Score
        newRound.setCurrentScore(600);
//        newRound.setCurrentScore(
//                Integer.parseInt(
//                        storage.getValue(
//                                storage.getRunChosen() - 1, DataParser.ROUND_SCORE_INDEX)));

        // Check if the deck is empty (all "NA")
        boolean allRestOfDeckEmpty = true;
        for (int i = DataParser.START_OF_REST_OF_DECK;
                i < DataParser.START_OF_REST_OF_DECK + 44;
                i++) {
            String cardValue = storage.getValue(storage.getRunChosen() - 1, i);
            if (!cardValue.equals("-") && !cardValue.equals("NA")) {
                allRestOfDeckEmpty = false;
                break;
            }
        }

        // Check if the holding hand is empty (all "NA")
        boolean allHoldingHandEmpty = true;
        List<Card> savedCards = new ArrayList<>();
        for (int i = DataParser.HOLDING_HAND_START_INDEX;
                i < DataParser.HOLDING_HAND_START_INDEX + 8;
                i++) {
            String cardValue = storage.getValue(storage.getRunChosen() - 1, i);
            if (!cardValue.equals("-") && !cardValue.equals("NA")) {
                allHoldingHandEmpty = false;
                savedCards.add(CardUtils.parseCardString(cardValue));
            }
        }

        // Only update deck if it is not empty
        if (!allHoldingHandEmpty && !allRestOfDeckEmpty) {
            newRound.getDeck().populateWithSavedDeck();
            newRound.setPlayerHandCards(savedCards);
        }

        // Update Jokers
        heldJokers = new HeldJokers();
        for (int i = DataParser.JOKER_HAND_START_INDEX;
                i < DataParser.JOKER_HAND_START_INDEX + 5;
                i++) {
            String jokerName = storage.getValue(storage.getRunChosen() - 1, i);
            if (!jokerName.equals("-") && !jokerName.equals("NA")) {
                try {
                    heldJokers.add(CardUtils.parseJokerString(jokerName));
                } catch (JavatroException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        startNewRound(newRound);
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
