// @@author Markneoneo
package javatro.display.screens;

import static javatro.display.UI.BOLD;
import static javatro.display.UI.BORDER_WIDTH;
import static javatro.display.UI.BOTTOM_LEFT;
import static javatro.display.UI.BOTTOM_RIGHT;
import static javatro.display.UI.END;
import static javatro.display.UI.HORIZONTAL;
import static javatro.display.UI.RED;
import static javatro.display.UI.TOP_LEFT;
import static javatro.display.UI.TOP_RIGHT;
import static javatro.display.UI.T_LEFT;
import static javatro.display.UI.T_RIGHT;
import static javatro.display.UI.YELLOW;
import static javatro.display.UI.centerText;
import static javatro.display.UI.getCardArtLines;
import static javatro.display.UI.printBlackB;
import static javatro.display.UI.printBorderedContent;

import javatro.core.Card;
import javatro.core.HoldingHand;
import javatro.core.JavatroCore;
import javatro.core.JavatroException;
import javatro.manager.options.CardSelectOption;
import javatro.manager.options.DeckViewOption;
import javatro.manager.options.PokerHandOption;
import javatro.manager.options.ResumeGameOption;
import javatro.manager.options.SortByRankOption;
import javatro.manager.options.SortBySuitOption;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class for card selection screens with sorting and display capabilities.
 *
 * <p>Provides common functionality for:
 *
 * <ul>
 *   <li>Displaying formatted card hands
 *   <li>Handling card sorting operations
 *   <li>Managing selection options
 *   <li>Maintaining card display state
 * </ul>
 */
public abstract class CardSelectScreen extends Screen {

    /** Default spacing between card indices in the title display */
    private static final int CARD_INDEX_SPACING = 8;

    /** Number of lines in the card art display */
    private static final int CARD_ART_LINES = 5;

    /** Current player's hand of cards */
    private List<Card> holdingHand;

    /** Active sorting order for card display */
    private SortOrder currentSortOrder;

    /**
     * Constructs a card selection screen with core functionality.
     *
     * @param optionsTitle Title for the options section
     * @throws JavatroException if optionsTitle is invalid
     */
    public CardSelectScreen(String optionsTitle) throws JavatroException {
        super(optionsTitle);

        // Initialize selection commands
        super.commandMap.add(new CardSelectOption());
        super.commandMap.add(new SortBySuitOption(this));
        super.commandMap.add(new SortByRankOption(this));
        super.commandMap.add(new PokerHandOption());
        super.commandMap.add(new DeckViewOption());
        super.commandMap.add(new ResumeGameOption());

        assert commandMap.size() == 6 : "Should have 6 initial commands";
    }

    /**
     * Updates and sorts the displayed hand according to specified order.
     *
     * @param sortOrder Desired sorting order (null preserves current order)
     */
    public void updateHoldingHand(SortOrder sortOrder) {
        this.holdingHand = new ArrayList<>(JavatroCore.currentRound.getPlayerHandCards());
        this.currentSortOrder = sortOrder != null ? sortOrder : SortOrder.ORIGINAL;

        if (sortOrder != null && sortOrder != SortOrder.ORIGINAL) {
            HoldingHand tempHand = new HoldingHand();
            tempHand.Hand = new ArrayList<>(this.holdingHand);

            switch (sortOrder) {
                case BY_SUIT:
                    tempHand.sortBySuit();
                    break;
                case BY_RANK:
                    tempHand.sortByRank();
                    break;
                default:
                    // Maintain original order
                    break;
            }

            this.holdingHand = tempHand.getHand();
            assert this.holdingHand.size() == tempHand.getHand().size()
                    : "Sorting should not change card count";
        }

        JavatroCore.currentRound.setPlayerHandCards(this.holdingHand);
    }

    /**
     * Generates formatted card index header for display.
     *
     * @param cardCount Number of cards in hand
     * @return Formatted string with numbered card positions
     */
    protected String getCardIndicesTitle(int cardCount) {
        assert cardCount >= 0 : "Card count cannot be negative";

        StringBuilder title = new StringBuilder();
        for (int i = 1; i <= cardCount; i++) {
            title.append(YELLOW).append(BOLD).append("<").append(i).append(">");
            if (i < cardCount) {
                title.append(" ".repeat(CARD_INDEX_SPACING));
            }
        }
        title.append(END);
        return title.toString();
    }

    /** Displays formatted card hand with borders and styling. */
    public void displayHoldingHand() {
        updateHoldingHand(currentSortOrder);

        if (holdingHand.isEmpty()) {
            printBorderedContent(
                    "CURRENT HOLDING HAND", List.of(RED + "YOU HAVE NO MORE CARDS!" + END));
            return;
        }

        final int cardCount = holdingHand.size();
        assert cardCount > 0 : "Card count should be positive when not empty";

        // Render top border
        printBlackB(TOP_LEFT + String.valueOf(HORIZONTAL).repeat(BORDER_WIDTH - 2) + TOP_RIGHT);
        System.out.println();

        // Display card indices
        printBlackB(centerText(getCardIndicesTitle(cardCount), 100));
        System.out.println();

        // Middle divider
        printBlackB(T_RIGHT + String.valueOf(HORIZONTAL).repeat(BORDER_WIDTH - 2) + T_LEFT);
        System.out.println();

        // Display card art
        List<String> cardArtLines = getCardArtLines(holdingHand);
        assert cardArtLines.size() == CARD_ART_LINES
                : "Card art should have " + CARD_ART_LINES + " lines";

        for (int i = 0; i < cardArtLines.size(); i++) {
            String line = cardArtLines.get(i);
            int width = (i == 2) ? 100 : BORDER_WIDTH; // Special width for middle line
            printBlackB(centerText(line, width));
            System.out.println();
        }

        // Render bottom border
        printBlackB(
                BOTTOM_LEFT + String.valueOf(HORIZONTAL).repeat(BORDER_WIDTH - 2) + BOTTOM_RIGHT);
        System.out.println();
    }

    /** Sorting options for card display */
    public enum SortOrder {
        /** Original deal order */
        ORIGINAL,
        /** Grouped by card suit */
        BY_SUIT,
        /** Ordered by card rank */
        BY_RANK
    }

    /** Implemented by subclasses to define screen-specific display logic */
    @Override
    public abstract void displayScreen();
}
