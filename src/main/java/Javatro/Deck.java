package Javatro;

import javatro_core.Card;

import java.util.ArrayList;
import java.util.Collections;

/*
 * Holds all the free cards the player has
 * Contains an ArrayList of type Card: with 0 being the top of the deck
 * and ArrayList.size() being the bottom
 */
public class Deck {
    private static ArrayList<Card> deck;

    /**
     * Initialize the deck with cards that the player owns If no new cards owned or a new game has
     * started, initializes a new deck
     */
    public Deck() {
        deck = populateNewDeck();
    }

    /** Draws and returns a card from the top of the deck. */
    public Card draw() {
        return deck.remove(0);
    }

    /** Returns an integer containing the cards left in the deck */
    public int getRemainingCards() {
        return deck.size();
    }

    /**
     * Initialize a new shuffled 52 card deck for a new game Consists of the standard Poker Deck: 13
     * Cards of the 4 Suits
     */
    private ArrayList<Card> populateNewDeck() {
        ArrayList<Card> newDeck = new ArrayList<Card>();
        for (Card.Rank rank : Card.Rank.values()) {
            for (Card.Suit suit : Card.Suit.values()) {
                newDeck.add(new Card(rank, suit));
            }
        }
        Collections.shuffle(newDeck);
        return newDeck;
    }
}
