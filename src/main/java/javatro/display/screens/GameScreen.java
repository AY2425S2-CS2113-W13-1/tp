// @@author Markneoneo
package javatro.display.screens;

import static javatro.display.UI.BLACK_B;
import static javatro.display.UI.BLUE;
import static javatro.display.UI.BLUE_B;
import static javatro.display.UI.BOLD;
import static javatro.display.UI.BORDER_WIDTH;
import static javatro.display.UI.BOTTOM_LEFT;
import static javatro.display.UI.BOTTOM_RIGHT;
import static javatro.display.UI.CROSS;
import static javatro.display.UI.END;
import static javatro.display.UI.HORIZONTAL;
import static javatro.display.UI.ITALICS;
import static javatro.display.UI.ORANGE;
import static javatro.display.UI.ORANGE_B;
import static javatro.display.UI.PURPLE_B;
import static javatro.display.UI.RED;
import static javatro.display.UI.TOP_LEFT;
import static javatro.display.UI.TOP_RIGHT;
import static javatro.display.UI.T_DOWN;
import static javatro.display.UI.T_LEFT;
import static javatro.display.UI.T_RIGHT;
import static javatro.display.UI.T_UP;
import static javatro.display.UI.VERTICAL;
import static javatro.display.UI.WHITE;
import static javatro.display.UI.YELLOW;
import static javatro.display.UI.centerText;
import static javatro.display.UI.getCardArtLines;
import static javatro.display.UI.getDisplayLength;
import static javatro.display.UI.padToWidth;

import javatro.core.Card;
import javatro.core.JavatroCore;
import javatro.core.JavatroException;
import javatro.core.jokers.Joker;
import javatro.manager.options.DeckViewOption;
import javatro.manager.options.DiscardCardOption;
import javatro.manager.options.ExitGameOption;
import javatro.manager.options.MainMenuOption;
import javatro.manager.options.PlayCardOption;
import javatro.manager.options.PokerHandOption;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * GameScreen class represents the game menu screen in the Javatro application. It displays game
 * information such as round details, scores, deck view, and options, and listens to property
 * changes to update the display accordingly.
 *
 * <p>This class extends {@link Screen} and implements {@link java.beans.PropertyChangeListener} to
 * react to changes in game state.
 */
public class GameScreen extends Screen implements PropertyChangeListener {

    // --- Static Fields ---

    /** The blind score that needs to be beaten. */
    private static int blindScore = 0;

    /** The current round score. */
    private static long roundScore = 0;

    /** The number of hands left to play. */
    private static int handsLeft = 0;

    /** The number of discards left. */
    private static int discardsLeft = 0;

    /** The list of cards currently held in the player's hand. */
    private static List<Card> holdingHand;

    /**
     * Width of each column in the game screen display. (For example, 100 = 32 + 32 + 32 + 4
     * borders)
     */
    private static final int COLUMN_WIDTH = 32;

    // --- Instance Fields ---

    /** The name of the current round. */
    private String roundName = "";

    /** Description of the current round. */
    private String roundDescription = "";

    /**
     * Constructs a new GameScreen with predefined command options.
     *
     * @throws JavatroException if there is an error initializing the screen.
     */
    public GameScreen() throws JavatroException {
        super("GAME MENU");
        // Add command options to the game screen command map.
        commandMap.add(new PlayCardOption());
        commandMap.add(new DiscardCardOption());
        commandMap.add(new PokerHandOption());
        commandMap.add(new DeckViewOption());
        commandMap.add(new MainMenuOption());
        commandMap.add(new ExitGameOption());
    }

    /**
     * Displays the game screen with current game information such as scores, round details, deck
     * view, and card art.
     */
    @Override
    public void displayScreen() {
        // Use StringBuilder for efficient string concatenation.
        StringBuilder sb = new StringBuilder();

        // --- Top Border ---
        sb.append(BLACK_B)
                .append(TOP_LEFT)
                .append(String.valueOf(HORIZONTAL).repeat(BORDER_WIDTH - 2))
                .append(TOP_RIGHT)
                .append(END)
                .append("\n");

        // --- Round Name Color Determination ---
        // Determine the background color based on the round name.
        String colourb = BLACK_B;
        if (Objects.equals(roundName, "SMALL BLIND")) {
            colourb = BLUE_B;
        } else if (Objects.equals(roundName, "LARGE BLIND")) {
            colourb = ORANGE_B;
        } else if (Objects.equals(roundName, JavatroCore.currentRound.getConfig().getRoundName())) {
            colourb = PURPLE_B;
        }

        // Calculate display length accounting for ANSI codes and Unicode characters.
        int displayLength = getDisplayLength(roundName);
        // Calculate padding to center the text within the border.
        int paddingSize = (BORDER_WIDTH - displayLength - 2) / 2;
        int extraPadding = (BORDER_WIDTH - displayLength - 2) % 2; // Handles odd width cases

        // --- Round Name Line ---
        sb.append(BLACK_B)
                .append(VERTICAL)
                .append(colourb)
                .append(" ".repeat(paddingSize))
                .append(WHITE)
                .append(BOLD)
                .append(roundName)
                .append(" ".repeat(paddingSize + extraPadding))
                .append(END)
                .append(BLACK_B)
                .append(VERTICAL)
                .append(END)
                .append("\n");

        // --- Round Description Centered ---
        sb.append(centerText(ITALICS + roundDescription + END, BORDER_WIDTH)).append("\n");

        // --- Separator Border (First) ---
        sb.append(BLACK_B)
                .append(T_RIGHT)
                .append(String.valueOf(HORIZONTAL).repeat(COLUMN_WIDTH))
                .append(T_DOWN)
                .append(String.valueOf(HORIZONTAL).repeat(COLUMN_WIDTH))
                .append(T_DOWN)
                .append(String.valueOf(HORIZONTAL).repeat(COLUMN_WIDTH))
                .append(T_LEFT)
                .append(END)
                .append("\n");

        // --- Blind Score / Ante / Round Information ---
        String bs =
                String.format("%s%sScore to beat: %d%s%s", YELLOW, BOLD, blindScore, END, BLACK_B);
        String bScore = centerText(bs, COLUMN_WIDTH + 2);
        String anteCount =
                String.format("          Ante: %d / 8", JavatroCore.getAnte().getAnteCount());
        String roundCount = String.format("            Round: %d", JavatroCore.getRoundCount());
        // Construct the row with vertical borders.
        sb.append(bScore)
                .append(BLACK_B)
                .append(BOLD)
                .append(ORANGE)
                .append(padToWidth(anteCount, COLUMN_WIDTH))
                .append(END)
                .append(BLACK_B)
                .append(VERTICAL)
                .append(BOLD)
                .append(ORANGE)
                .append(padToWidth(roundCount, COLUMN_WIDTH))
                .append(END)
                .append(BLACK_B)
                .append(VERTICAL)
                .append(END)
                .append("\n");

        // --- Separator Border (Second) ---
        sb.append(BLACK_B)
                .append(T_RIGHT)
                .append(String.valueOf(HORIZONTAL).repeat(COLUMN_WIDTH))
                .append(CROSS)
                .append(String.valueOf(HORIZONTAL).repeat(COLUMN_WIDTH))
                .append(CROSS)
                .append(String.valueOf(HORIZONTAL).repeat(COLUMN_WIDTH))
                .append(T_LEFT)
                .append(END)
                .append("\n");

        // --- Round Score / Hands / Discards Information ---
        String rs =
                String.format("%s%sRound Score: %d%s%s", YELLOW, BOLD, roundScore, END, BLACK_B);
        String rScore = centerText(rs, COLUMN_WIDTH + 2);
        String handCount = String.format("            Hands: %d", handsLeft);
        String discardCount = String.format("          Discards: %d", discardsLeft);
        // Construct the row with vertical borders.
        sb.append(rScore)
                .append(BLACK_B)
                .append(BOLD)
                .append(BLUE)
                .append(padToWidth(handCount, COLUMN_WIDTH))
                .append(END)
                .append(BLACK_B)
                .append(VERTICAL)
                .append(BOLD)
                .append(RED)
                .append(padToWidth(discardCount, COLUMN_WIDTH))
                .append(END)
                .append(BLACK_B)
                .append(VERTICAL)
                .append(END)
                .append("\n");

        // --- Separator Border (Third) ---
        sb.append(BLACK_B)
                .append(T_RIGHT)
                .append(String.valueOf(HORIZONTAL).repeat(COLUMN_WIDTH))
                .append(CROSS)
                .append(String.valueOf(HORIZONTAL).repeat(COLUMN_WIDTH))
                .append(T_UP)
                .append(String.valueOf(HORIZONTAL).repeat(COLUMN_WIDTH))
                .append(T_LEFT)
                .append(END)
                .append("\n");

        // --- Empty Row for Spacing ---
        sb.append(BLACK_B)
                .append(VERTICAL)
                .append(" ".repeat(COLUMN_WIDTH))
                .append(VERTICAL)
                .append(" ".repeat(COLUMN_WIDTH * 2 + 1))
                .append(VERTICAL)
                .append(END)
                .append("\n");

        // --- Deck Name, Jokers' Effects, and Holding Hand ---
        // Build extra content lines for deck name and joker effects.
        List<String> extraContent = new ArrayList<>();
        extraContent.add(BOLD + "Current Deck:");
        extraContent.add(ITALICS + JavatroCore.deck.getDeckName().getName());
        extraContent.add("");
        extraContent.add("");
        extraContent.add(BOLD + "Jokers' Effects:");

        // Iterate through held jokers and add their effect description or "Empty Joker Slot" if
        // null.
        List<Joker> jokers = JavatroCore.heldJokers.getJokers();
        for (int i = 0; i < 5; i++) {
            if (i < jokers.size() && jokers.get(i) != null) {
                extraContent.add(ITALICS + jokers.get(i).toString());
            } else {
                extraContent.add(ITALICS + "Empty Joker Slot");
            }
        }

        // --- Card Art for Holding Hand ---
        // Ensure that holdingHand is initialized and has at least 8 cards.
        assert holdingHand != null : "Holding hand should not be null";
        assert holdingHand.size() >= 8 : "Holding hand must contain at least 8 cards";

        // Split the holding hand into two groups for side-by-side display.
        List<Card> firstHalf = holdingHand.subList(0, 4);
        List<Card> secondHalf = holdingHand.subList(4, 8);

        // Get card art for both halves.
        List<String> firstCardArt = getCardArtLines(firstHalf);
        List<String> secondCardArt = getCardArtLines(secondHalf);

        // --- Display First Half of Card Art with Extra Content ---
        for (int i = 0; i < firstCardArt.size(); i++) {
            // Get the corresponding line of extra content.
            String extraLine = extraContent.get(i);
            // Get the corresponding card art line.
            String cardLine = firstCardArt.get(i);

            sb.append(centerText(extraLine, COLUMN_WIDTH + 2))
                    .append(BLACK_B)
                    .append(" ".repeat(11))
                    .append(cardLine)
                    .append(BLACK_B)
                    .append(" ".repeat(12))
                    .append(VERTICAL)
                    .append(END)
                    .append("\n");
        }

        // --- Spacer Row ---
        sb.append(BLACK_B)
                .append(VERTICAL)
                .append(" ".repeat(COLUMN_WIDTH))
                .append(VERTICAL)
                .append(" ".repeat(COLUMN_WIDTH * 2 + 1))
                .append(VERTICAL)
                .append(END)
                .append("\n");

        // --- Display Second Half of Card Art with Extra Content ---
        for (int i = 0; i < secondCardArt.size(); i++) {
            // Get the corresponding line of extra content (offset by first half size).
            String extraLine = extraContent.get(i + firstCardArt.size());
            // Get the corresponding card art line.
            String cardLine = secondCardArt.get(i);

            sb.append(centerText(extraLine, COLUMN_WIDTH + 2))
                    .append(BLACK_B)
                    .append(" ".repeat(12))
                    .append(cardLine)
                    .append(BLACK_B)
                    .append(" ".repeat(11))
                    .append(VERTICAL)
                    .append(END)
                    .append("\n");
        }

        // --- Spacer Row ---
        sb.append(BLACK_B)
                .append(VERTICAL)
                .append(" ".repeat(COLUMN_WIDTH))
                .append(VERTICAL)
                .append(" ".repeat(COLUMN_WIDTH * 2 + 1))
                .append(VERTICAL)
                .append(END)
                .append("\n");

        // --- Bottom Border ---
        sb.append(BLACK_B)
                .append(BOTTOM_LEFT)
                .append(String.valueOf(HORIZONTAL).repeat(COLUMN_WIDTH))
                .append(T_UP)
                .append(String.valueOf(HORIZONTAL).repeat(COLUMN_WIDTH * 2 + 1))
                .append(BOTTOM_RIGHT)
                .append(END);

        // Finally, print the complete screen.
        System.out.println(sb);
    }

    /**
     * Handles property change events to update game screen information.
     *
     * @param evt the property change event containing the updated property name and value.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        Object newValue = evt.getNewValue();

        // Map property names to their respective update actions.
        Map<String, Consumer<Object>> handlers = new HashMap<>();
        handlers.put("roundName", v -> roundName = v.toString());
        handlers.put("roundDescription", v -> roundDescription = v.toString());
        handlers.put("remainingPlays", v -> handsLeft = (Integer) v);
        handlers.put("remainingDiscards", v -> discardsLeft = (Integer) v);
        handlers.put("blindScore", v -> blindScore = (Integer) v);
        handlers.put("currentScore", v -> roundScore = (Long) v);

        // Handle the 'holdingHand' property change by filtering and casting the list.
        handlers.put(
                "holdingHand",
                v -> {
                    @SuppressWarnings("unchecked")
                    List<?> rawList = (List<?>) v;
                    // Assert that the raw list is not null.
                    assert rawList != null : "Raw list for holdingHand should not be null";
                    holdingHand =
                            rawList.stream()
                                    .filter(Card.class::isInstance)
                                    .map(Card.class::cast)
                                    .collect(Collectors.toList());
                });

        // Execute the corresponding handler if one exists.
        handlers.getOrDefault(propertyName, val -> {}).accept(newValue);
    }
}
