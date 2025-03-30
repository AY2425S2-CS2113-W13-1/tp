package javatro.display.screens;

import javatro.core.Card;
import javatro.core.JavatroCore;
import javatro.core.JavatroException;
import javatro.display.UI;
import javatro.manager.options.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * The {@code GameScreen} class represents the game screen where players interact with the game by
 * playing cards, discarding cards, and viewing their hand. It also listens for property changes to
 * update the game state dynamically.
 */
public class GameScreen extends Screen implements PropertyChangeListener {

    /** Indicator for whether the round is over. 1 for won, -1 for lost, 0 for ongoing. */
    public static int roundOver = 0;

    /** The fixed width of the screen for display formatting. */
    private static final int screenWidth = 80;

    /** The score required to pass the round. */
    private static int blindScore = 0;
    /** The player's score for the current round. */
    private static long roundScore = 0;
    /** The number of hands left to play. */
    private static int handsLeft = 0;
    /** The number of discards remaining. */
    private static int discardsLeft = 0;
    /** The player's current hand of cards. */
    private static List<Card> holdingHand;

    /** The name of the current round. */
    private String roundName = "";
    /** The description of the current round. */
    private String roundDescription = "";

    /** Constructs a {@code GameScreen} and initializes the available commands. */
    public GameScreen() throws JavatroException {
        super("GAME MENU");
        commandMap.add(new PlayCardOption());
        commandMap.add(new DiscardCardOption());
        commandMap.add(new MainMenuOption());
        commandMap.add(new ExitGameOption());
    }

    /** Restores the default game commands after the round ends. */
    public void restoreGameCommands() {
        commandMap.clear();
        commandMap.add(new PlayCardOption());
        commandMap.add(new DiscardCardOption());
        commandMap.add(new DeckOption());
        commandMap.add(new MainMenuOption());
        commandMap.add(new ExitGameOption());
    }

    /**
     * Formats a string to be centered within a specified width.
     *
     * @param toDisplay The string to center.
     * @param width The width to center within.
     * @return The formatted string.
     */
    public static String getDisplayStringCenter(String toDisplay, int width) {

        if (toDisplay.length() > width - 2) {
            toDisplay = toDisplay.substring(0, width - 2);
        }

        StringBuilder toReturn = new StringBuilder();
        int numberOfBlanks = (width - toDisplay.length()) / 2;
        toReturn.append(" ".repeat(Math.max(0, numberOfBlanks)));
        toReturn.append(toDisplay);
        toReturn.append(" ".repeat(Math.max(0, numberOfBlanks)));
        if (numberOfBlanks * 2 + toDisplay.length() < width) {
            int blanksToAppend = width - (numberOfBlanks * 2 + toDisplay.length());
            toReturn.append(" ".repeat(Math.max(0, blanksToAppend)));
        }

        return toReturn.toString();
    }

    /**
     * Formats a string to be left-aligned within a specified width.
     *
     * @param toDisplay The string to align.
     * @param width The width to align within.
     * @return The formatted string.
     */
    private static String getDisplayStringLeft(String toDisplay, int width) {

        if (toDisplay.length() > width - 2) {
            toDisplay = toDisplay.substring(0, width - 2);
        }

        StringBuilder toReturn = new StringBuilder("|");
        int numberOfBlanks = width - toDisplay.length() - 1;
        toReturn.append(" " + toDisplay);
        toReturn.append(" ".repeat(Math.max(0, numberOfBlanks)));
        toReturn.append("|");

        return toReturn.toString();
    }

    /**
     * Generates a header string for table formatting.
     *
     * @param width The width of the header.
     * @return The formatted header string.
     */
    public static String getHeaderString(int width) {
        String headerString = "+";
        headerString += "-".repeat(width);
        headerString += "+";
        return headerString;
    }

    /**
     * Displays the round name and status.
     *
     * @param header The formatted header string.
     */
    private void displayRoundName(String header) {
        System.out.println(header);
        String winStatus = roundOver == 1 ? "YOU WON!" : roundOver == -1 ? "YOU LOST!" : "";
        if (roundOver == 1 || roundOver == -1) {
            System.out.println("|" + getDisplayStringCenter(winStatus, screenWidth) + "|");
        } else {
            System.out.println("|" + getDisplayStringCenter(roundName, screenWidth) + "|");
        }
        System.out.println(header);
    }

    /** Displays the round description and the current game status. */
    private void displayRoundDesc() {
        // Prints round description + score (on left hand side) and Current Table Text (on right
        // hand side)
        System.out.println(
                "|"
                        + getDisplayStringCenter(roundDescription, screenWidth / 8 * 3)
                        + "|"
                        + getDisplayStringCenter("Jokers", (screenWidth / 8 * 5) - 1)
                        + "|");
    }
    /**
     * Displays the Joker Cards On The Game
     *
     * @param header The formatted header string.
     */
    private void displayJokers(String header) {
        // Get the card header for all 5 cards chosen to play
        String cardHeaders = (getHeaderString(5) + "  ").repeat(5);

        System.out.println(
                "|"
                        + getDisplayStringCenter("", screenWidth / 8 * 3)
                        + "|"
                        + getDisplayStringCenter(cardHeaders, (screenWidth / 8 * 5) - 1)
                        + "|");

        String cardValues = "";
        for (int i = 0; i < 5; i++) {
            cardValues += "|" + getDisplayStringCenter("X", 5) + "|" + "  ";
        }

        // Printing the card value itself
        System.out.println(
                "|"
                        + getDisplayStringCenter(
                                "Score at least " + blindScore, screenWidth / 8 * 3)
                        + "|"
                        + getDisplayStringCenter(cardValues, (screenWidth / 8 * 5) - 1)
                        + "|");

        // Printing the bottom header
        System.out.println(
                "|"
                        + getDisplayStringCenter("", screenWidth / 8 * 3)
                        + "|"
                        + getDisplayStringCenter(cardHeaders, (screenWidth / 8 * 5) - 1)
                        + "|");

        // Print end header
        System.out.println(header);
    }

    /** Displays the round score and the player's current hand. */
    private void displayRoundScore() {
        // Printing round score (left hand side) and your hand (right hand side)
        System.out.println(
                "|"
                        + getDisplayStringCenter("Round Score: " + roundScore, screenWidth / 8 * 3)
                        + "|"
                        + getDisplayStringCenter("Your Hand: ", (screenWidth / 8 * 5) - 1)
                        + "|");
    }

    /**
     * Generates a formatted string representation of the cards.
     *
     * @param hand The list of cards.
     * @param start The starting index.
     * @param end The ending index.
     * @return The formatted string.
     */
    private String generateCardValues(List<Card> hand, int start, int end) {
        StringBuilder values = new StringBuilder();
        for (int i = start; i < end; i++) {
            values.append("|")
                    .append(getDisplayStringCenter(hand.get(i).getSimplified(), 5))
                    .append("|  ");
        }
        return values.toString();
    }

    /**
     * Prints a formatted row with left and right-aligned content.
     *
     * @param leftText The left-aligned text.
     * @param rightText The right-aligned text.
     */
    private void printCardRow(String leftText, String rightText) {
        System.out.println(
                "|"
                        + getDisplayStringCenter(leftText, screenWidth / 8 * 3)
                        + "|"
                        + getDisplayStringCenter(rightText, (screenWidth / 8 * 5) - 1)
                        + "|");
    }

    private void printRoundOver() {
        String title = "::: " + UI.BOLD + "Round Ended" + " :::" + UI.END;
        String message;
        if (roundOver == 1) {
            message = "YOU WON!!!!";
        } else {
            message = "YOU LOST!!!!";
        }

        String[] lines = {
            "", "", message, "", "",
        };

        UI.printBorderedContent(title, List.of(lines));
    }

    /** Displays the game screen on the display */
    @Override
    public void displayScreen() {
        if (roundOver != 0) {
            printRoundOver();
        }

        String header = getHeaderString(screenWidth);

        // Prints the round name
        displayRoundName(header);

        // Displays round description + Hand Chosen To Play
        displayRoundDesc();

        // Displays Jokers
        displayJokers(header);

        displayRoundScore();

        // Your hand, it can have up to 8 cards, so print 4 on first row, 4 on second row
        int firstRowCount = Math.min(4, holdingHand.size());
        int secondRowCount = Math.max(0, holdingHand.size() - firstRowCount);

        String firstRowHeader = (getHeaderString(5) + "  ").repeat(firstRowCount);
        String firstRowValues = generateCardValues(holdingHand, 0, firstRowCount);

        printCardRow("", firstRowHeader);
        printCardRow("", firstRowValues);

        // display hands and discards info along with second row headers (if any cards left)
        String handsAndDiscards = "Hands: " + handsLeft + "  Discards: " + discardsLeft;
        String secondRowHeader = (getHeaderString(5) + "  ").repeat(secondRowCount);
        printCardRow(handsAndDiscards, secondRowHeader);

        // display cash info
        printCardRow("Cash: $ - ", "");

        // display ante info
        printCardRow("Ante: " + JavatroCore.getAnte().getAnteCount() + " / 8", secondRowHeader);

        // Generate the values for the second row (if applicable)
        if (secondRowCount > 0) {
            String secondRowValues =
                    generateCardValues(holdingHand, firstRowCount, holdingHand.size());
            printCardRow("", secondRowValues);
        }

        // display current round info
        printCardRow("Current Round: " + JavatroCore.getRoundCount(), secondRowHeader);

        // Print end header
        System.out.println(header);
    }

    /**
     * Updates the display when a change to a game variable occurs in the Round Class.
     *
     * @param evt The property change event containing the updated value.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        String propertyName = evt.getPropertyName();
        Object newValue = evt.getNewValue();

        // Map for property handlers
        Map<String, Consumer<Object>> propertyHandlers = new HashMap<>();

        propertyHandlers.put("roundName", value -> roundName = value.toString());
        propertyHandlers.put("remainingPlays", value -> handsLeft = (Integer) value);
        propertyHandlers.put("remainingDiscards", value -> discardsLeft = (Integer) value);
        propertyHandlers.put("currentScore", value -> roundScore = (Long) value);
        propertyHandlers.put("roundDescription", value -> roundDescription = value.toString());
        propertyHandlers.put("blindScore", value -> blindScore = (Integer) value);
        propertyHandlers.put(
                "holdingHand",
                value -> {
                    List<?> list = (List<?>) value;
                    holdingHand =
                            list.stream()
                                    .filter(
                                            Card.class
                                                    ::isInstance) // Ensures only Card instances are
                                    // collected
                                    .map(Card.class::cast) // Safely cast to Card
                                    .collect(Collectors.toList());
                });
        propertyHandlers.put(
                "roundComplete",
                value -> {
                    roundOver = (Integer) value;
                    if (roundOver == 1) {
                        commandMap.clear();
                        commandMap.add(new NextRoundOption());
                        commandMap.add(new ExitGameOption());
                    } else if (roundOver == -1) {
                        commandMap.clear();
                        commandMap.add(new MainMenuOption());
                        commandMap.add(new ExitGameOption());
                    }
                });

        // Execute the appropriate handler if it exists and update its value
        propertyHandlers.getOrDefault(propertyName, v -> {}).accept(newValue);
    }
}
