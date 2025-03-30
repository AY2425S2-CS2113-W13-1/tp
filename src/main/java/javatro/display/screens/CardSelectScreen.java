package javatro.display.screens;

import static javatro.display.UI.BLACK_B;
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
import static javatro.display.UI.printBlackB;
import static javatro.display.UI.printBorderedContent;

import javatro.core.Card;
import javatro.core.HoldingHand;
import javatro.core.JavatroCore;
import javatro.core.JavatroException;
import javatro.display.CardRenderer;
import javatro.manager.options.CardSelectOption;
import javatro.manager.options.PokerHandOption;
import javatro.manager.options.ResumeGameOption;
import javatro.manager.options.SortByRankOption;
import javatro.manager.options.SortBySuitOption;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@code CardSelectScreen} class represents an abstract screen where users select cards from
 * their hand. It provides methods for updating and displaying the player's current hand. This class
 * is intended to be extended by specific screens like {@code DiscardScreen} and {@code PlayScreen}.
 *
 * <p>The class includes functionality to:
 *
 * <ul>
 *   <li>Update the player's current hand of cards.
 *   <li>Display the player's hand in a formatted layout.
 *   <li>Provide a default selection limit for card selection.
 * </ul>
 *
 * @see Screen
 */
public abstract class CardSelectScreen extends Screen {

    /** The list of cards currently in the player's hand. */
    private List<Card> holdingHand;

    private SortOrder currentSortOrder;

    /**
     * Constructs a {@code CardSelectScreen} with a custom options title and initializes it with a
     * resume game command.
     *
     * @param optionsTitle The title to display for the option menu.
     * @throws JavatroException if an error occurs during initialization.
     * @throws IllegalArgumentException if the options title is null or empty.
     */
    public CardSelectScreen(String optionsTitle) throws JavatroException {
        super(optionsTitle);

        // Add the "Select Cards" and "Resume Game" options
        super.commandMap.add(new CardSelectOption());
        super.commandMap.add(new SortBySuitOption(this));
        super.commandMap.add(new SortByRankOption(this));
        super.commandMap.add(new PokerHandOption());
        super.commandMap.add(new ResumeGameOption());
    }

    /**
     * Updates the holding hand by retrieving the player's current hand from the game core.
     *
     * @param sortOrder The sorting order to apply (null for no sorting)
     */
    public void updateHoldingHand(SortOrder sortOrder) {
        // Get current Holding Hand
        this.holdingHand = new ArrayList<>(JavatroCore.currentRound.getPlayerHand());
        this.currentSortOrder = sortOrder != null ? sortOrder : SortOrder.ORIGINAL;

        // Apply sorting if requested
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
                default: // Should not happen at all.
                    break;
            }

            this.holdingHand = tempHand.getHand();
        }
        // Update holding hand for proper selection index
        JavatroCore.currentRound.playerHand.setHand(holdingHand);
    }

    /**
     * Generates and returns the formatted card indices title string for the player's hand.
     *
     * @param cardCount the number of cards in the hand
     * @return formatted string showing card indices with proper spacing and coloring
     */
    protected String getCardIndicesTitle(int cardCount) {
        StringBuilder title = new StringBuilder();
        for (int i = 1; i <= cardCount; i++) {
            title.append(YELLOW).append(BOLD).append("<").append(i).append(">");
            // Only add spacing if not the last element
            if (i < cardCount) {
                //                title.append(HAIR_SPACE.repeat(18));
                title.append(" ".repeat(8));
            }
        }
        title.append(END);
        return title.toString();
    }

    /**
     * Generates a list of strings representing the ASCII art lines for all cards in the hand.
     *
     * @param holdingHand the list of cards to render
     * @return List of strings where each string represents a line of card art
     */
    protected List<String> getCardArtLines(List<Card> holdingHand) {
        List<String> cardArtLines = new ArrayList<>();
        int cardCount = holdingHand.size();
        int cardLength = 5; // Number of lines per card

        // Render each card into its ASCII art representation
        String[][] renderedCards = new String[cardCount][cardLength];
        for (int i = 0; i < cardCount; i++) {
            renderedCards[i] = CardRenderer.renderCard(holdingHand.get(i));
        }

        // Combine the card art line by line
        for (int line = 0; line < cardLength; line++) {
            StringBuilder lineBuilder = new StringBuilder();
            for (int i = 0; i < cardCount; i++) {
                lineBuilder.append(renderedCards[i][line]);
                if (i < cardCount - 1) { // Add space only if there's another card after this one
                    lineBuilder.append(BLACK_B + "  " + END);
                }
            }
            cardArtLines.add(lineBuilder.toString());
        }

        return cardArtLines;
    }

    /**
     * Displays the player's current hand of cards in a formatted layout with borders. The cards are
     * displayed with their indices and ASCII art, with special formatting for the middle line of
     * card art.
     */
    public void displayHoldingHand() {

        // Sort hand if chosen
        updateHoldingHand(currentSortOrder);

        if (holdingHand.isEmpty()) {
            printBorderedContent(
                    "CURRENT HOLDING HAND", List.of(RED + "YOU HAVE NO MORE CARDS!" + END));
            return;
        }

        int cardCount = holdingHand.size();

        // Top border
        printBlackB(TOP_LEFT + String.valueOf(HORIZONTAL).repeat(BORDER_WIDTH - 2) + TOP_RIGHT);
        System.out.println();

        // Card Indices title
        String indicesTitle = getCardIndicesTitle(cardCount);
        printBlackB(centerText(indicesTitle, 100)); // 81
        System.out.println();

        // Middle border
        printBlackB(T_RIGHT + String.valueOf(HORIZONTAL).repeat(BORDER_WIDTH - 2) + T_LEFT);
        System.out.println();

        // Get and display card art
        List<String> cardArtLines = getCardArtLines(holdingHand);
        for (int i = 0; i < cardArtLines.size(); i++) {
            String line = cardArtLines.get(i);
            // Apply a different border style for the 3rd line (index 2)
            if (i == 2) {
                printBlackB(centerText(line, 100)); // 110
            } else {
                printBlackB(centerText(line, BORDER_WIDTH));
            }
            System.out.println();
        }

        // Bottom border
        printBlackB(
                BOTTOM_LEFT + String.valueOf(HORIZONTAL).repeat(BORDER_WIDTH - 2) + BOTTOM_RIGHT);
        System.out.println();
    }

    /**
     * Displays the screen. This method is intended to be overridden by subclasses to provide
     * specific screen display behavior.
     */
    @Override
    public abstract void displayScreen();

    // Add an enum for sorting options
    public enum SortOrder {
        ORIGINAL,
        BY_SUIT,
        BY_RANK,
    }
}
