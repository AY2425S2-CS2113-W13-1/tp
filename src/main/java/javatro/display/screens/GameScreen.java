package javatro.display.screens;

import static javatro.display.UI.*;
import static javatro.display.UI.centerText;

import javatro.core.Card;
import javatro.core.JavatroCore;
import javatro.core.JavatroException;
import javatro.core.jokers.Joker;
import javatro.manager.options.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class GameScreen extends Screen implements PropertyChangeListener {

    private static int blindScore = 0;
    private static long roundScore = 0;
    private static int handsLeft = 0;
    private static int discardsLeft = 0;
    private static List<Card> holdingHand;

    private String roundName = "";
    private String roundDescription = "";

    public GameScreen() throws JavatroException {
        super("GAME MENU");
        commandMap.add(new PlayCardOption());
        commandMap.add(new DiscardCardOption());
        commandMap.add(new PokerHandOption());
        commandMap.add(new DeckViewOption());
        commandMap.add(new MainMenuOption());
        commandMap.add(new ExitGameOption());
    }

    // 100 = 32 + 32 + 32 + 4 borders
    private static final int COLUMN_WIDTH = 32;

    @Override
    public void displayScreen() {
        clearScreen();
        StringBuilder sb = new StringBuilder();

        // --- Top Border ---
        sb.append(BLACK_B)
                .append(TOP_LEFT)
                .append(String.valueOf(HORIZONTAL).repeat(BORDER_WIDTH - 2))
                .append(TOP_RIGHT)
                .append(END)
                .append("\n");

        // --- Blind Name / Description ---
        String blindHeader = BOLD + PURPLE + roundName + END;
        String blindDesc = ITALICS + roundDescription + END;
        sb.append(centerText(blindHeader, BORDER_WIDTH)).append("\n");
        sb.append(centerText(blindDesc, BORDER_WIDTH)).append("\n");

        // --- Separator Border ---
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

        // --- Blind Score / Ante / Round ---
        String bs = String.format("Score to beat: %d", blindScore);
        String bScore = centerText(bs, COLUMN_WIDTH + 2);
        String anteCount =
                String.format("          Ante: %d / 8", JavatroCore.getAnte().getAnteCount());
        String roundCount = String.format("            Round: %d", JavatroCore.getRoundCount());
        // Print the row with vertical borders.
        sb.append(bScore)
                .append(BLACK_B)
                .append(padToWidth(anteCount, COLUMN_WIDTH))
                .append(VERTICAL)
                .append(padToWidth(roundCount, COLUMN_WIDTH))
                .append(VERTICAL)
                .append(END)
                .append("\n");

        // --- Separator Border ---
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

        // --- Round Score / Hands / Discards ---
        String rs = String.format("Round Score: %d", roundScore);
        String rScore = centerText(rs, COLUMN_WIDTH + 2);
        String handCount = String.format("            Hands: %d", handsLeft);
        String discardCount = String.format("          Discards: %d", discardsLeft);
        // Print the row with vertical borders.
        sb.append(rScore)
                .append(BLACK_B)
                .append(padToWidth(handCount, COLUMN_WIDTH))
                .append(VERTICAL)
                .append(padToWidth(discardCount, COLUMN_WIDTH))
                .append(VERTICAL)
                .append(END)
                .append("\n");

        // --- Separator Border ---
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

        sb.append(BLACK_B)
                .append(VERTICAL)
                .append(" ".repeat(COLUMN_WIDTH))
                .append(VERTICAL)
                .append(" ".repeat(COLUMN_WIDTH * 2 + 1))
                .append(VERTICAL)
                .append(END)
                .append("\n");

        // --- Deck Name / Jokers / Holding Hand ---
        List<String> extraContent = new ArrayList<>();
        extraContent.add("Current Deck:");
        extraContent.add(JavatroCore.deck.getDeckName().getName());
        extraContent.add("");
        extraContent.add("");
        extraContent.add("Jokers' Effects:");

        // Iterate through heldJokers and print their toString() or "Empty" if null
        List<Joker> jokers = JavatroCore.heldJokers.getJokers();
        for (int i = 0; i < 5; i++) {
            if (i < jokers.size() && jokers.get(i) != null) {
                extraContent.add(jokers.get(i).toString());
            } else {
                extraContent.add("No Joker Available");
            }
        }

        // Split into two groups of 4
        List<Card> firstHalf = holdingHand.subList(0, 4);
        List<Card> secondHalf = holdingHand.subList(4, 8);

        // Get card art for both halves
        List<String> firstCardArt = getCardArtLines(firstHalf);
        List<String> secondCardArt = getCardArtLines(secondHalf);

        // Print side-by-side
        for (int i = 0; i < firstCardArt.size(); i++) {
            String extraLine = extraContent.get(i); // Get the corresponding line of extra content
            String cardLine = firstCardArt.get(i); // Get the corresponding line of card art

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

        sb.append(BLACK_B)
                .append(VERTICAL)
                .append(" ".repeat(COLUMN_WIDTH))
                .append(VERTICAL)
                .append(" ".repeat(COLUMN_WIDTH * 2 + 1))
                .append(VERTICAL)
                .append(END)
                .append("\n");

        // Print side-by-side
        for (int i = 0; i < secondCardArt.size(); i++) {
            String extraLine =
                    extraContent.get(
                            i + firstCardArt.size()); // Get the corresponding line of extra content
            String cardLine = secondCardArt.get(i); // Get the corresponding line of card art

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

        // Finally, print the complete table.
        System.out.println(sb);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        Object newValue = evt.getNewValue();

        Map<String, Consumer<Object>> handlers = new HashMap<>();
        handlers.put("roundName", v -> roundName = v.toString());
        handlers.put("roundDescription", v -> roundDescription = v.toString());
        handlers.put("remainingPlays", v -> handsLeft = (Integer) v);
        handlers.put("remainingDiscards", v -> discardsLeft = (Integer) v);
        handlers.put("blindScore", v -> blindScore = (Integer) v);
        handlers.put("currentScore", v -> roundScore = (Long) v);

        handlers.put(
                "holdingHand",
                v -> {
                    List<?> rawList = (List<?>) v;
                    holdingHand =
                            rawList.stream()
                                    .filter(Card.class::isInstance)
                                    .map(Card.class::cast)
                                    .collect(Collectors.toList());
                });

        handlers.getOrDefault(propertyName, val -> {}).accept(newValue);
    }
}
