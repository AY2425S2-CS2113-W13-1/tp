// @@ Markneoneo
package javatro.display;

import static javatro.display.UI.BLUE;
import static javatro.display.UI.BOLD;
import static javatro.display.UI.END;
import static javatro.display.UI.ORANGE;
import static javatro.display.UI.PURPLE;
import static javatro.display.UI.RED;
import static javatro.display.UI.WHITE_B;

import javatro.core.Card;

import java.util.Arrays;

/**
 * Renders playing cards as ASCII art with colored symbols.
 *
 * <p>Each card is rendered as a multi-line string array with:
 *
 * <ul>
 *   <li>Colored suit symbols
 *   <li>Proper rank display
 *   <li>White card background
 *   <li>Consistent 5-line height formatting
 * </ul>
 */
public final class CardRenderer {

    /** Number of lines in the rendered card art. */
    private static final int CARD_HEIGHT = 5;

    /**
     * Renders a card as an array of strings representing ASCII art lines.
     *
     * <p>The rendered card has:
     *
     * <ul>
     *   <li>Rank in top-left and bottom-right corners
     *   <li>Suit symbol centered
     *   <li>White background with colored symbols
     * </ul>
     *
     * @param card the card to render (cannot be null)
     * @return String array of length 5 containing the rendered card lines
     * @throws NullPointerException if card parameter is null
     */
    public static String[] renderCard(Card card) {
        assert card != null : "Card cannot be null";
        assert card.rank() != null : "Card rank cannot be null";
        assert card.suit() != null : "Card suit cannot be null";

        String rank = card.rank().getSymbol();
        String suitSymbol = getSuitSymbol(card.suit());
        String colour = getColour(card.suit());

        String[] cardArt = new String[CARD_HEIGHT];
        cardArt[0] = WHITE_B + String.format(" %s%s%-2s      ", colour, BOLD, rank) + END;
        cardArt[1] = WHITE_B + "         " + END;
        cardArt[2] = WHITE_B + String.format("    %s%s    ", colour, suitSymbol) + END;
        cardArt[3] = WHITE_B + "         " + END;
        cardArt[4] = WHITE_B + String.format("      %s%s%2s ", colour, BOLD, rank) + END;

        assert cardArt.length == CARD_HEIGHT
                : "Card art must have exactly " + CARD_HEIGHT + " lines";
        assert Arrays.stream(cardArt).noneMatch(s -> s == null || s.isEmpty())
                : "All card art lines must be non-empty";

        return cardArt;
    }

    /**
     * Maps card suit to its corresponding single-character symbol.
     *
     * @param suit the card suit to map
     * @return single-character symbol representing the suit
     * @throws NullPointerException if suit parameter is null
     */
    private static String getSuitSymbol(Card.Suit suit) {
        assert suit != null : "Suit cannot be null";

        return switch (suit) {
            case HEARTS -> "H";
            case DIAMONDS -> "D";
            case CLUBS -> "C";
            case SPADES -> "S";
        };
    }

    /**
     * Determines the display color for a given card suit.
     *
     * @param suit the card suit to determine color for
     * @return ANSI color code string for the suit
     * @throws NullPointerException if suit parameter is null
     */
    private static String getColour(Card.Suit suit) {
        assert suit != null : "Suit cannot be null";

        return switch (suit) {
            case HEARTS -> RED;
            case DIAMONDS -> ORANGE;
            case CLUBS -> BLUE;
            case SPADES -> PURPLE;
        };
    }
}
