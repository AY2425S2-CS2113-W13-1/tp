// @@author flyingapricot
package javatro.storage.utils;

import static javatro.core.Ante.Blind.BOSS_BLIND;
import static javatro.core.Ante.Blind.LARGE_BLIND;
import static javatro.core.Ante.Blind.SMALL_BLIND;
import static javatro.display.DeckArt.ABANDONED_DECK;
import static javatro.display.DeckArt.BLUE_DECK;
import static javatro.display.DeckArt.CHECKERED_DECK;
import static javatro.display.DeckArt.RED_DECK;

import javatro.core.Ante;
import javatro.core.Card;
import javatro.core.Deck;
import javatro.core.jokers.Joker;
import javatro.core.jokers.addchip.OddToddJoker;
import javatro.core.jokers.addchip.ScaryFaceJoker;
import javatro.core.jokers.addmult.AbstractJoker;
import javatro.core.jokers.addmult.GluttonousJoker;
import javatro.core.jokers.addmult.GreedyJoker;
import javatro.core.jokers.addmult.HalfJoker;
import javatro.core.jokers.addmult.LustyJoker;
import javatro.core.jokers.addmult.WrathfulJoker;
import javatro.display.DeckArt;

/**
 * The {@code CardUtils} class provides utility methods for handling cards, decks, blinds, and
 * jokers in the Javatro application. It provides various conversion, parsing, and validation
 * functionalities.
 *
 * <p>This class is designed to be used statically and should not be instantiated.
 */
public class CardUtils {

    /**
     * Converts a storage key into a {@code DeckArt} object.
     *
     * @param key The key representing the deck type.
     * @return The corresponding {@code DeckArt}.
     * @throws IllegalArgumentException If the key is invalid.
     */
    public static DeckArt fromStorageKey(String key) {
        assert key != null : "Deck key cannot be null";
        return switch (key.toUpperCase()) {
        case "RED" -> RED_DECK;
        case "BLUE" -> BLUE_DECK;
        case "CHECKERED" -> CHECKERED_DECK;
        case "ABANDONED" -> ABANDONED_DECK;
        default -> throw new IllegalArgumentException("Unknown deck art: " + key);
        };
    }

    /**
     * Converts a storage key into a {@code Deck.DeckType} object.
     *
     * @param key The key representing the deck type.
     * @return The corresponding {@code Deck.DeckType}.
     * @throws IllegalArgumentException If the key is invalid.
     */
    public static Deck.DeckType deckFromKey(String key) {
        assert key != null : "Deck key cannot be null";
        return switch (key.toUpperCase()) {
        case "RED" -> Deck.DeckType.RED;
        case "BLUE" -> Deck.DeckType.BLUE;
        case "CHECKERED" -> Deck.DeckType.CHECKERED;
        case "ABANDONED" -> Deck.DeckType.ABANDONED;
        default -> throw new IllegalArgumentException("Unknown deck type: " + key);
        };
    }

    /**
     * Converts a storage key into an {@code Ante.Blind} object.
     *
     * @param key The key representing the blind type.
     * @return The corresponding {@code Ante.Blind}.
     * @throws IllegalArgumentException If the key is invalid.
     */
    public static Ante.Blind blindFromKey(String key) {
        assert key != null : "Blind key cannot be null";
        return switch (key.toUpperCase()) {
        case "SMALL BLIND" -> SMALL_BLIND;
        case "LARGE BLIND" -> LARGE_BLIND;
        case "BOSS BLIND" -> BOSS_BLIND;
        default -> throw new IllegalArgumentException("Unknown blind type: " + key);
        };
    }

    /**
     * Parses a card string into a {@code Card} object.
     *
     * @param cardString The card string to parse.
     * @return The corresponding {@code Card}.
     * @throws IllegalArgumentException If the card string is invalid.
     */
    public static Card parseCardString(String cardString) {
        assert cardString != null : "Card string cannot be null";
        assert cardString.length() >= 2 : "Card string is too short";

        String rankStr = cardString.substring(0, cardString.length() - 1);
        char suitChar = cardString.charAt(cardString.length() - 1);

        Card.Rank rank =
            switch (rankStr) {
            case "2" -> Card.Rank.TWO;
            case "3" -> Card.Rank.THREE;
            case "4" -> Card.Rank.FOUR;
            case "5" -> Card.Rank.FIVE;
            case "6" -> Card.Rank.SIX;
            case "7" -> Card.Rank.SEVEN;
            case "8" -> Card.Rank.EIGHT;
            case "9" -> Card.Rank.NINE;
            case "10" -> Card.Rank.TEN;
            case "J" -> Card.Rank.JACK;
            case "Q" -> Card.Rank.QUEEN;
            case "K" -> Card.Rank.KING;
            case "A" -> Card.Rank.ACE;
            default -> throw new IllegalArgumentException("Invalid rank: " + rankStr);
        };

        Card.Suit suit =
            switch (Character.toUpperCase(suitChar)) {
            case 'H' -> Card.Suit.HEARTS;
            case 'C' -> Card.Suit.CLUBS;
            case 'S' -> Card.Suit.SPADES;
            case 'D' -> Card.Suit.DIAMONDS;
            default -> throw new IllegalArgumentException("Invalid suit: " + suitChar);
        };

        return new Card(rank, suit);
    }

    /**
     * Converts a {@code Card} to a string representation.
     *
     * @param card The {@code Card} to convert.
     * @return The string representation of the card.
     */
    public static String cardToString(Card card) {
        assert card != null : "Card cannot be null";
        return card.rank().getSymbol()
            + switch (card.suit()) {
            case HEARTS -> "H";
            case CLUBS -> "C";
            case SPADES -> "S";
            case DIAMONDS -> "D";
            };
    }

    /**
     * Validates if a given card string is in the correct format.
     *
     * @param cardString The card string to validate.
     * @return {@code true} if the card string is valid, otherwise {@code false}.
     */
    public static boolean isValidCardString(String cardString) {
        assert cardString != null : "Card string cannot be null";
        if (cardString.length() < 2) {
            return false;
        }

        String rankStr = cardString.substring(0, cardString.length() - 1);
        char suitChar = cardString.charAt(cardString.length() - 1);

        boolean isValidRank =
            switch (rankStr) {
            case "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A" -> true;
            default -> false;
        };

        boolean isValidSuit =
            switch (Character.toUpperCase(suitChar)) {
            case 'H', 'C', 'S', 'D' -> true;
            default -> false;
        };

        return isValidRank && isValidSuit;
    }

    /**
     * Parses a joker name string into a {@code Joker} object.
     *
     * @param jokerName The name of the joker.
     * @return The corresponding {@code Joker} object.
     */
    public static Joker parseJokerString(String jokerName) {
        assert jokerName != null : "Joker name cannot be null";
        return switch (jokerName.toUpperCase()) {
        case "ODDTODDJOKER" -> new OddToddJoker();
        case "SCARYFACEJOKER" -> new ScaryFaceJoker();
        case "ABSTRACTJOKER" -> new AbstractJoker();
        case "GLUTTONOUSJOKER" -> new GluttonousJoker();
        case "GREEDYJOKER" -> new GreedyJoker();
        case "HALFJOKER" -> new HalfJoker();
        case "LUSTYJOKER" -> new LustyJoker();
        case "WRATHFULJOKER" -> new WrathfulJoker();
        default -> throw new IllegalArgumentException("Invalid Joker name: " + jokerName);
        };
    }

    /**
     * Converts a {@code Joker} object to its string representation.
     *
     * @param joker The {@code Joker} object to convert.
     * @return The string representation of the {@code Joker}.
     * @throws IllegalArgumentException if the joker is null.
     */
    public static String jokerToString(Joker joker) {
        assert joker != null : "Joker cannot be null";
        return joker.getIdentifierName(); // Returns the class name of the Joker
    }

    /**
     * Validates if a given joker name is valid.
     *
     * @param jokerName The name of the joker to validate.
     * @return {@code true} if the name is valid, otherwise {@code false}.
     */
    public static boolean isValidJokerString(String jokerName) {
        assert jokerName != null : "Joker name cannot be null";
        return switch (jokerName.toUpperCase()) {
        case "ODDTODDJOKER", "SCARYFACEJOKER" -> true;
        default -> false;
        };
    }
}
