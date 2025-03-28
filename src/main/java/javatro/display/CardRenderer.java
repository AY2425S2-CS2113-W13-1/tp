package javatro.display;

import static javatro.display.UI.*;

import javatro.core.Card;

public class CardRenderer {

    /**
     * Renders a card as an array of strings, each representing one line of ASCII art.
     *
     * @param card The card to render.
     * @return A String array containing the lines of the rendered card.
     */
    public static String[] renderCard(Card card) {
        String rank = card.rank().getSymbol();
        String suitSymbol = getSuitSymbol(card.suit());
        String leftSpacing = HAIR_SPACE.repeat(14);
        String rightSpacing = getRightSpacing(card.suit());
        String colour = getColour(card.suit());

        // The card art is 7 lines tall
        String[] cardArt = new String[5];
        cardArt[0] = WHITE_B + String.format(" %s%s%-2s      ", colour, BOLD, rank) + END;
        cardArt[1] = WHITE_B + "         " + END;
        cardArt[2] = WHITE_B + leftSpacing + suitSymbol + rightSpacing + END;
        cardArt[3] = WHITE_B + "         " + END;
        cardArt[4] = WHITE_B + String.format("      %s%s%2s ", colour, BOLD, rank) + END;
        return cardArt;
    }

    /**
     * Maps the card suit to its Unicode symbol.
     *
     * @param suit The suit of the card.
     * @return A string containing the Unicode symbol for the suit.
     */
    private static String getSuitSymbol(Card.Suit suit) {
        return switch (suit) {
            case HEARTS -> "♥️";
            case DIAMONDS -> "♦️";
            case CLUBS -> "♣️";
            case SPADES -> "♠️";
                //            case HEARTS -> "♥";
                //            case DIAMONDS -> "♦";
                //            case CLUBS -> "♣";
                //            case SPADES -> "♠";
        };
    }

    // Custom spacing because diff suits have diff spacing
    private static String getRightSpacing(Card.Suit suit) {
        return switch (suit) {
            case HEARTS, CLUBS -> HAIR_SPACE.repeat(13);
            case DIAMONDS -> HAIR_SPACE.repeat(13);
            case SPADES -> HAIR_SPACE.repeat(11) + THIN_SPACE;
        };
    }

    private static String getColour(Card.Suit suit) {
        return switch (suit) {
            case HEARTS, DIAMONDS -> UI.RED;
            case CLUBS, SPADES -> UI.BLACK;
        };
    }
}
