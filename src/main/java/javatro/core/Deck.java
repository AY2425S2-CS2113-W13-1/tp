package javatro.core;

import javatro.storage.DataParser;
import javatro.storage.Storage;
import javatro.storage.StorageManager;
import javatro.storage.utils.CardUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/*
 * Holds all the free cards the player has
 * Contains an ArrayList of type Card: with 0 being the top of the deck
 * and ArrayList.size() being the bottom
 */
public class Deck {

    private ArrayList<Card> deck;
    private DeckType deckType;

    /**
     * Initialize the deck with cards that the player owns If no new cards owned or a new game has
     * started, initializes a new deck
     */
    public Deck(DeckType deckType) {
        this.deckType = deckType;
        this.deck = populateNewDeck(deckType);
    }

    /** Copy Constructor for deck class */
    Deck(Deck oldDeck) {
        this.deck = new ArrayList<Card>(oldDeck.deck.size());
        this.deckType = oldDeck.deckType;
        for (Card card : oldDeck.deck) {
            this.deck.add(new Card(card));
        }
    }

    /** Draws and returns a card from the top of the deck */
    public Card draw() throws JavatroException {
        try {

            return deck.remove(0);
        } catch (IndexOutOfBoundsException e) {
            throw JavatroException.noCardsRemaining();
        }
    }

    /** Returns an integer containing the cards left in the deck */
    public int getRemainingCards() {
        return deck.size();
    }

    /** Returns an ArrayList containing all the remaining cards in the deck */
    public ArrayList<Card> getWholeDeck() {
        ArrayList<Card> wholeDeckList = new ArrayList<Card>(deck.size());
        for (Card card : deck) {
            wholeDeckList.add(new Card(card));
        }

        return wholeDeckList;
    }

    /** Returns an DeckType containing the deck variant you are using */
    public DeckType getDeckName() {
        return deckType;
    }

    /** Shuffle the deck you are using */
    public void shuffle() {
        Collections.shuffle(deck);
    }

    /** Initialize a new deck for the game, based on the deckType given. */
    private ArrayList<Card> populateNewDeck(DeckType deckType) {
        ArrayList<Card> newDeck = new ArrayList<Card>();

        //
        if (deckType == DeckType.CHECKERED) {
            newDeck = populateNewCheckeredDeck();
        } else if (deckType == DeckType.ABANDONED) {
            newDeck = populateNewAbandonedDeck();
        } else {
            newDeck = populateDefaultDeck();
        }
        assert newDeck != null;

        return newDeck;
    }

    /**
     * Initialize a new shuffled 52 card deck for a new game Consists of the standard Poker Deck: 13
     * Cards of the 4 Suits
     */
    private ArrayList<Card> populateDefaultDeck() {
        ArrayList<Card> newDeck = new ArrayList<Card>();
        for (Card.Rank rank : Card.Rank.values()) {
            for (Card.Suit suit : Card.Suit.values()) {
                newDeck.add(new Card(rank, suit));
            }
        }
        Collections.shuffle(newDeck);
        return newDeck;
    }

    public void populateWithSavedDeck() {
        Storage storage = Storage.getStorageInstance();
        int runIndex = storage.getRunChosen() - 1;

        // Retrieve current run data
        ArrayList<String> runData = StorageManager.getInstance().getRunData(runIndex);
        assert runData != null : "Run data should not be null";
        assert runData.size() >= DataParser.START_OF_REST_OF_DECK + 44
                : "Run data is incomplete or corrupted";

        ArrayList<Card> newDeck = new ArrayList<>();

        // Parse all cards in one go
        for (int i = DataParser.START_OF_REST_OF_DECK;
                i < DataParser.START_OF_REST_OF_DECK + 44;
                i++) {
            String cardString = runData.get(i);

            if (!cardString.equals("-") && !cardString.equals("NA")) {
                try {
                    newDeck.add(CardUtils.parseCardString(cardString));
                } catch (IllegalArgumentException e) {
                    System.out.println("Failed to parse card string: " + cardString);
                }
            }
        }

        Storage.isNewDeck = false;

        // Shuffle the deck and update the local deck variable
        Collections.shuffle(newDeck);
        deck = new ArrayList<>(newDeck);

        // Save the updated deck to storage
        StorageManager.getInstance().saveRunData(runIndex, runData);

        try {
            storage.updateSaveFile();
        } catch (JavatroException e) {
            System.out.println("Failed to save updated deck to file.");
        }
    }

    /**
     * Initialize a new shuffled 52 card deck for a new game Consists of the standard Poker Deck: 26
     * Cards of the 2 Suites: Spades and Hearts.
     */
    private ArrayList<Card> populateNewCheckeredDeck() {
        ArrayList<Card> newDeck = new ArrayList<Card>();
        for (Card.Rank rank : Card.Rank.values()) {
            // Populate for Hearts
            Card.Suit suitHeart = Card.Suit.valueOf(Card.Suit.HEARTS.toString());
            newDeck.add(new Card(rank, suitHeart));
            newDeck.add(new Card(rank, suitHeart));
            // Populate for Spades
            Card.Suit suitSpades = Card.Suit.valueOf(Card.Suit.SPADES.toString());
            newDeck.add(new Card(rank, suitSpades));
            newDeck.add(new Card(rank, suitSpades));
        }
        Collections.shuffle(newDeck);
        return newDeck;
    }

    /**
     * Initialize a new shuffled 52 card deck for a new game Consists of the standard Poker Deck: 26
     * Cards of the 2 Suites: Spades and Hearts.
     */
    private ArrayList<Card> populateNewAbandonedDeck() {
        ArrayList<Card> newDeck = new ArrayList<Card>();
        Arrays.stream(Card.Rank.values())
                .filter(
                        rank ->
                                rank != Card.Rank.KING
                                        && rank != Card.Rank.QUEEN
                                        && rank != Card.Rank.JACK)
                .forEach(
                        rank -> {
                            Arrays.stream(Card.Suit.values())
                                    .forEach(suit -> newDeck.add(new Card(rank, suit)));
                        });
        Collections.shuffle(newDeck);
        return newDeck;
    }

    /**
     * Enum representing the type of the deck. Test Deck is not to be used, and is a default deck.
     */
    public enum DeckType {
        ABANDONED("ABANDONED"),
        BLUE("BLUE"),
        CHECKERED("CHECKERED"),
        RED("RED"),
        DEFAULT("DEFAULT");

        private final String name;

        DeckType(String name) {
            this.name = name;
        }

        /**
         * Returns the symbol of the rank.
         *
         * @return The symbol of the rank.
         */
        public String getName() {
            return name;
        }
    }
}
