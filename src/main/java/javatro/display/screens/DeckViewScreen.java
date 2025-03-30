package javatro.display.screens;

import static javatro.display.UI.BLACK_B;
import static javatro.display.UI.BLUE;
import static javatro.display.UI.BLUE_B;
import static javatro.display.UI.BOLD;
import static javatro.display.UI.BOTTOM_LEFT;
import static javatro.display.UI.BOTTOM_RIGHT;
import static javatro.display.UI.CROSS;
import static javatro.display.UI.END;
import static javatro.display.UI.HORIZONTAL;
import static javatro.display.UI.ORANGE;
import static javatro.display.UI.ORANGE_B;
import static javatro.display.UI.PURPLE;
import static javatro.display.UI.PURPLE_B;
import static javatro.display.UI.RED;
import static javatro.display.UI.RED_B;
import static javatro.display.UI.TOP_LEFT;
import static javatro.display.UI.TOP_RIGHT;
import static javatro.display.UI.T_DOWN;
import static javatro.display.UI.T_LEFT;
import static javatro.display.UI.T_RIGHT;
import static javatro.display.UI.T_UP;
import static javatro.display.UI.VERTICAL;
import static javatro.display.UI.WHITE;
import static javatro.display.UI.centerText;
import static javatro.display.UI.padToWidth;

import javatro.core.Card;
import javatro.core.Deck;
import javatro.core.JavatroException;
import javatro.core.Round;
import javatro.manager.options.ReturnOption;

import java.util.ArrayList;

/**
 * DeckViewScreen prints the current deck as a two-dimensional table with totals. - The rows
 * represent card suits (Spades, Hearts, Clubs, Diamonds) with an extra column at the end showing
 * the total number of cards in that suit. - The columns represent card ranks in descending order:
 * A, K, Q, J, 10, 9, …, 2, with an extra row at the bottom showing the total count for each rank.
 * The bottom right cell displays the grand total.
 */
public class DeckViewScreen extends Screen {

    // Overall dimensions: total width = LEFT_WIDTH + RIGHT_WIDTH + 3 (for the vertical borders)
    private static final int LEFT_WIDTH = 17; // For deck name or suit names (including suit totals)
    private static final int RIGHT_WIDTH = 80; // For rank headers and the numbers matrix
    // Total width = 17 + 80 + 3 = 100

    /**
     * Constructs a screen with the specified options title.
     *
     * @throws JavatroException if the options title is null or empty
     */
    public DeckViewScreen() throws JavatroException {
        super("Your Current Deck");
        super.commandMap.add(new ReturnOption());
    }

    @Override
    public void displayScreen() {

        Deck deck = Round.deck;
        String deckType = String.valueOf(deck.getDeckName());
        ArrayList<Card> remainingCardList = deck.getWholeDeck();

        // Step 1: Build the data matrix.
        // There are 4 suits and 13 ranks.
        int[][] counts = new int[4][13];
        for (Card card : remainingCardList) {
            int suitIndex = getSuitIndex(card.suit());
            int rankIndex = getRankIndex(card.rank());
            if (suitIndex != -1 && rankIndex != -1) {
                counts[suitIndex][rankIndex]++;
            }
        }

        // Step 2: Compute totals.
        int[] suitTotals = new int[4]; // Total cards per suit.
        int[] rankTotals = new int[13]; // Total cards per rank.
        int grandTotal = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 13; j++) {
                suitTotals[i] += counts[i][j];
                rankTotals[j] += counts[i][j];
                grandTotal += counts[i][j];
            }
        }

        // Define the order of ranks and suits.
        // X-axis (columns): A, K, Q, J, 10, 9, ... , 2.
        String[] rankOrder = {"A", "K", "Q", "J", "10", "9", "8", "7", "6", "5", "4", "3", "2"};
        // Y-axis (rows): Spades, Hearts, Clubs, Diamonds.
        String[] suitOrder = {"Spades", "Hearts", "Clubs", "Diamonds"};
        // Suit Colours
        String[] suitColour = {PURPLE, RED, BLUE, ORANGE};
        // Suit Colour Backgrounds
        String[] suitColourB = {PURPLE_B, RED_B, BLUE_B, ORANGE_B};

        // Step 3: Build the 4-box table manually.
        StringBuilder sb = new StringBuilder();

        // --- Top Border (spanning both boxes) ---
        // Format: TOP_LEFT + left box top border (LEFT_WIDTH chars) + T_DOWN + right box top border
        // (RIGHT_WIDTH chars) + TOP_RIGHT
        sb.append(BLACK_B)
                .append(TOP_LEFT)
                .append(String.valueOf(HORIZONTAL).repeat(LEFT_WIDTH))
                .append(T_DOWN)
                .append(String.valueOf(HORIZONTAL).repeat(RIGHT_WIDTH))
                .append(TOP_RIGHT)
                .append(END)
                .append("\n");

        // --------- Top Row Content: Left Box (Deck Name) and Right Box (Rank Header) ---------
        // Left box: deck name centered in LEFT_WIDTH.
        String deckName = centerText(deckType, LEFT_WIDTH + 2);
        // Right box: rank headers.
        // For 13 ranks, use 5 chars each, and for the total header use 15 chars.
        StringBuilder rankHeader = new StringBuilder();
        for (String rank : rankOrder) {
            rankHeader.append(String.format("%5s", rank));
        }
        rankHeader.append(String.format("%15s", "Total" + "     "));
        // Now, print the row with vertical borders.
        sb.append(BOLD)
                .append(deckName)
                .append(BOLD)
                .append(BLACK_B)
                .append(rankHeader)
                .append(VERTICAL)
                .append(END)
                .append("\n");

        // --- Separator between top and bottom boxes ---
        // Format: T_RIGHT + left box separator (LEFT_WIDTH chars) + CROSS + right box separator
        // (RIGHT_WIDTH chars) + T_LEFT
        sb.append(BLACK_B)
                .append(T_RIGHT)
                .append(String.valueOf(HORIZONTAL).repeat(LEFT_WIDTH))
                .append(CROSS)
                .append(String.valueOf(HORIZONTAL).repeat(RIGHT_WIDTH))
                .append(T_LEFT)
                .append(END)
                .append("\n");

        // --- Bottom Content Rows ---
        // We need to print 5 rows: 4 suit rows and 1 totals row (merged into the same box).
        // For each suit row:
        //        String[] suitSpace = {
        //            HAIR_SPACE.repeat(9) + THIN_SPACE,
        //            HAIR_SPACE.repeat(10),
        //            HAIR_SPACE.repeat(10),
        //            HAIR_SPACE.repeat(8)
        //        };
        for (int i = 0; i < suitOrder.length; i++) {
            // Left box: Suit label
            String suitLabel = String.format("%s%s%s", suitColour[i], suitOrder[i], END);
            String leftContent = centerText(suitLabel, LEFT_WIDTH + 2);
            // Right box: For this suit, print 13 numbers (each in 5 chars) then pad the totals
            // column with spaces (15 chars).
            StringBuilder rightContent = new StringBuilder();
            for (int j = 0; j < rankOrder.length; j++) {
                rightContent.append(String.format("%5d", counts[i][j]));
            }
            rightContent.append(String.format("%15s", suitTotals[i] + "      "));
            String rightRow = suitColourB[i] + padToWidth(rightContent.toString(), RIGHT_WIDTH);
            // Print the row with vertical borders.
            sb.append(BLACK_B)
                    .append(BOLD)
                    .append(leftContent)
                    .append(BLACK_B)
                    .append(WHITE)
                    .append(rightRow)
                    .append(END)
                    .append(BLACK_B)
                    .append(VERTICAL)
                    .append(END)
                    .append("\n");
        }
        // Totals row (for ranks) in bottom right, and label "Total" in bottom left.
        String leftTotals = centerText("Total", LEFT_WIDTH + 2);
        StringBuilder rightTotals = new StringBuilder();
        for (int j = 0; j < rankOrder.length; j++) {
            rightTotals.append(String.format("%5d", rankTotals[j]));
        }
        rightTotals.append(String.format("%9d", grandTotal));
        String rightTotalsRow = padToWidth(rightTotals.toString(), RIGHT_WIDTH);
        sb.append(BOLD)
                .append(leftTotals)
                .append(BLACK_B)
                .append(rightTotalsRow)
                .append(VERTICAL)
                .append(END)
                .append("\n");

        // --- Bottom Border ---
        // Format: BOTTOM_LEFT + left box bottom border (LEFT_WIDTH chars) + T_UP + right box bottom
        // border (RIGHT_WIDTH chars) + BOTTOM_RIGHT
        sb.append(BLACK_B)
                .append(BOTTOM_LEFT)
                .append(String.valueOf(HORIZONTAL).repeat(LEFT_WIDTH))
                .append(T_UP)
                .append(String.valueOf(HORIZONTAL).repeat(RIGHT_WIDTH))
                .append(BOTTOM_RIGHT)
                .append(END);

        // Finally, print the complete table.
        System.out.println(sb);
    }

    /**
     * Maps a card suit to a row index. Order: Spades -> 0, Hearts -> 1, Clubs -> 2, Diamonds -> 3.
     */
    private int getSuitIndex(Card.Suit suit) {
        return switch (suit) {
            case SPADES -> 0;
            case HEARTS -> 1;
            case CLUBS -> 2;
            case DIAMONDS -> 3;
            default -> -1;
        };
    }

    /** Maps a card rank to a column index based on the desired order: A, K, Q, J, 10, 9, …, 2. */
    private int getRankIndex(Card.Rank rank) {
        String symbol = rank.getSymbol();
        return switch (symbol) {
            case "A" -> 0;
            case "K" -> 1;
            case "Q" -> 2;
            case "J" -> 3;
            case "10" -> 4;
            case "9" -> 5;
            case "8" -> 6;
            case "7" -> 7;
            case "6" -> 8;
            case "5" -> 9;
            case "4" -> 10;
            case "3" -> 11;
            case "2" -> 12;
            default -> -1;
        };
    }
}
