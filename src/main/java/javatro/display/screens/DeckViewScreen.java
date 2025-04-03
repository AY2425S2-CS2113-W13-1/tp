// @@author Markneoneo
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
import javatro.core.JavatroCore;
import javatro.core.JavatroException;
import javatro.manager.options.ReturnOption;

import java.util.ArrayList;

/**
 * Displays the current deck composition in a formatted table view showing card distribution by suit
 * and rank.
 *
 * <p>The table layout includes:
 *
 * <ul>
 *   <li>Rows for each suit (Spades, Hearts, Clubs, Diamonds)
 *   <li>Columns for each rank (A, K, Q, J, 10 through 2)
 *   <li>Suit totals in the rightmost column
 *   <li>Rank totals in the bottom row
 *   <li>Grand total in the bottom-right cell
 * </ul>
 */
public class DeckViewScreen extends Screen {

    /** Width of the left section containing suit names */
    private static final int LEFT_WIDTH = 17;

    /** Width of the right section containing rank counts */
    private static final int RIGHT_WIDTH = 80;

    /** Total table width accounting for borders */
    private static final int TOTAL_WIDTH = LEFT_WIDTH + RIGHT_WIDTH + 3;

    /** Number of card suits in standard deck */
    private static final int SUIT_COUNT = 4;

    /** Number of card ranks in standard deck */
    private static final int RANK_COUNT = 13;

    /** Order of ranks for display (Ace high) */
    private static final String[] RANK_ORDER = {
        "A", "K", "Q", "J", "10", "9", "8", "7", "6", "5", "4", "3", "2"
    };

    /** Order of suits for display */
    private static final String[] SUIT_ORDER = {"Spades", "Hearts", "Clubs", "Diamonds"};

    /** Colors for each suit display */
    private static final String[] SUIT_COLORS = {PURPLE, RED, BLUE, ORANGE};

    /** Background colors for each suit display */
    private static final String[] SUIT_BACKGROUNDS = {PURPLE_B, RED_B, BLUE_B, ORANGE_B};

    /**
     * Constructs a deck view screen with return option.
     *
     * @throws JavatroException if screen initialization fails
     */
    public DeckViewScreen() throws JavatroException {
        super("Your Current Deck");
        super.commandMap.add(new ReturnOption());
    }

    /**
     * Displays the deck composition in a formatted table showing:
     *
     * <ul>
     *   <li>Card counts by suit and rank
     *   <li>Suit totals
     *   <li>Rank totals
     *   <li>Grand total count
     * </ul>
     */
    @Override
    public void displayScreen() {
        Deck deck = JavatroCore.currentRound.getDeck();
        assert deck != null : "Current deck should not be null";

        ArrayList<Card> remainingCards = deck.getWholeDeck();
        assert remainingCards != null : "Deck card list should not be null";

        // Build count matrix and calculate totals
        DeckCountData countData = buildCountMatrix(remainingCards);

        // Generate and print the formatted table
        System.out.println(buildDeckTable(deck.getDeckName().toString(), countData));
    }

    /**
     * Builds the count matrix and calculates totals from the deck cards.
     *
     * @param cards List of cards in the deck
     * @return DeckCountData containing counts and totals
     */
    private DeckCountData buildCountMatrix(ArrayList<Card> cards) {
        int[][] counts = new int[SUIT_COUNT][RANK_COUNT];
        int[] suitTotals = new int[SUIT_COUNT];
        int[] rankTotals = new int[RANK_COUNT];
        int grandTotal = 0;

        // Populate counts matrix
        for (Card card : cards) {
            int suitIndex = getSuitIndex(card.suit());
            int rankIndex = getRankIndex(card.rank());
            if (suitIndex != -1 && rankIndex != -1) {
                counts[suitIndex][rankIndex]++;
            }
        }

        // Calculate totals
        for (int i = 0; i < SUIT_COUNT; i++) {
            for (int j = 0; j < RANK_COUNT; j++) {
                suitTotals[i] += counts[i][j];
                rankTotals[j] += counts[i][j];
                grandTotal += counts[i][j];
            }
        }

        return new DeckCountData(counts, suitTotals, rankTotals, grandTotal);
    }

    /**
     * Builds the formatted deck display table.
     *
     * @param deckName Name of the current deck
     * @param countData Deck count statistics
     * @return Formatted table as StringBuilder
     */
    private StringBuilder buildDeckTable(String deckName, DeckCountData countData) {
        StringBuilder sb = new StringBuilder();

        // Table header section
        buildTableHeader(sb, deckName);

        // Suit rows section
        buildSuitRows(sb, countData);

        // Totals row section
        buildTotalsRow(sb, countData);

        // Table footer
        buildTableFooter(sb);

        return sb;
    }

    /** Builds the table header with deck name and rank headers */
    private void buildTableHeader(StringBuilder sb, String deckName) {
        // Top border
        sb.append(BLACK_B)
                .append(TOP_LEFT)
                .append(String.valueOf(HORIZONTAL).repeat(LEFT_WIDTH))
                .append(T_DOWN)
                .append(String.valueOf(HORIZONTAL).repeat(RIGHT_WIDTH))
                .append(TOP_RIGHT)
                .append(END)
                .append("\n");

        // Deck name and rank headers
        String deckHeader = centerText(deckName, LEFT_WIDTH + 2);
        String rankHeader = buildRankHeader();

        sb.append(BOLD)
                .append(deckHeader)
                .append(BOLD)
                .append(BLACK_B)
                .append(rankHeader)
                .append(VERTICAL)
                .append(END)
                .append("\n");

        // Header separator
        sb.append(BLACK_B)
                .append(T_RIGHT)
                .append(String.valueOf(HORIZONTAL).repeat(LEFT_WIDTH))
                .append(CROSS)
                .append(String.valueOf(HORIZONTAL).repeat(RIGHT_WIDTH))
                .append(T_LEFT)
                .append(END)
                .append("\n");
    }

    /** Builds the rank header line */
    private String buildRankHeader() {
        StringBuilder header = new StringBuilder();
        for (String rank : RANK_ORDER) {
            header.append(String.format("%5s", rank));
        }
        header.append(String.format("%15s", "Total     "));
        return header.toString();
    }

    /** Builds the suit rows with counts */
    private void buildSuitRows(StringBuilder sb, DeckCountData countData) {
        for (int i = 0; i < SUIT_ORDER.length; i++) {
            String suitLabel = String.format("%s%s%s", SUIT_COLORS[i], SUIT_ORDER[i], END);
            String leftContent = centerText(suitLabel, LEFT_WIDTH + 2);

            String rightContent = buildSuitCounts(i, countData.counts[i], countData.suitTotals[i]);
            String rightRow = SUIT_BACKGROUNDS[i] + padToWidth(rightContent, RIGHT_WIDTH);

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
    }

    /** Builds the counts string for a single suit */
    private String buildSuitCounts(int suitIndex, int[] counts, int suitTotal) {
        StringBuilder content = new StringBuilder();
        for (int j = 0; j < RANK_COUNT; j++) {
            content.append(String.format("%5d", counts[j]));
        }
        content.append(String.format("%15s", suitTotal + "      "));
        return content.toString();
    }

    /** Builds the totals row */
    private void buildTotalsRow(StringBuilder sb, DeckCountData countData) {
        String leftTotals = centerText("Total", LEFT_WIDTH + 2);
        String rightTotals = buildTotalsString(countData);

        sb.append(BOLD)
                .append(leftTotals)
                .append(BLACK_B)
                .append(rightTotals)
                .append(VERTICAL)
                .append(END)
                .append("\n");
    }

    /** Builds the totals string for rank totals and grand total */
    private String buildTotalsString(DeckCountData countData) {
        StringBuilder totals = new StringBuilder();
        for (int j = 0; j < RANK_COUNT; j++) {
            totals.append(String.format("%5d", countData.rankTotals[j]));
        }
        totals.append(String.format("%9d", countData.grandTotal));
        return padToWidth(totals.toString(), RIGHT_WIDTH);
    }

    /** Builds the table footer */
    private void buildTableFooter(StringBuilder sb) {
        sb.append(BLACK_B)
                .append(BOTTOM_LEFT)
                .append(String.valueOf(HORIZONTAL).repeat(LEFT_WIDTH))
                .append(T_UP)
                .append(String.valueOf(HORIZONTAL).repeat(RIGHT_WIDTH))
                .append(BOTTOM_RIGHT)
                .append(END);
    }

    /**
     * Maps card suit to display row index.
     *
     * @param suit Card suit to map
     * @return Row index (0-3) or -1 if invalid
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

    /**
     * Maps card rank to display column index based on RANK_ORDER.
     *
     * @param rank Card rank to map
     * @return Column index (0-12) or -1 if invalid
     */
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

    /** Helper class to organize deck count data. */
    private static class DeckCountData {
        final int[][] counts;
        final int[] suitTotals;
        final int[] rankTotals;
        final int grandTotal;

        DeckCountData(int[][] counts, int[] suitTotals, int[] rankTotals, int grandTotal) {
            this.counts = counts;
            this.suitTotals = suitTotals;
            this.rankTotals = rankTotals;
            this.grandTotal = grandTotal;
        }
    }
}
