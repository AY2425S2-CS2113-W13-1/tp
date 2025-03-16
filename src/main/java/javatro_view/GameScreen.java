package javatro_view;

import javatro_core.Card;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Objects;

// Displays the current round
public class GameScreen extends Screen implements PropertyChangeListener {

    // Obtain all these values from the model
    private String roundName = ""; // E.g. The Eye
    private String roundDescription = ""; // E.g. No repeat hand types this round
    private static int blindScore = 0;
    private static int roundScore = 0;
    private static int multLeft = 340;
    private static int multRight = 21_600;
    private static int handsLeft = 0;
    private static int discardsLeft = 0;
    private static int currentCash = 4;
    private static int anteLeft = 12;
    private static int anteRight = 8;
    private static int currentRoundNumber = 1;

    private static final int screenWidth = 80;

    // Set Colours
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    public GameScreen() {
        super("GAME MENU");
    }

    // Given a string to display in the game, adjust and centralise and return it
    private static String getDisplayStringCenter(String toDisplay, int width) {

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

    // Given a string to display in the game, adjust and left align and return it
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

    // Displays the "+-----+"
    private static String getHeaderString(int width) {
        String headerString = "+";
        headerString += "-".repeat(width);
        headerString += "+";
        return headerString;
    }

    public static void displayCard(Card c) {
        String cardSimplified = c.getSimplified();
        String header = getHeaderString(5);

        System.out.println(header);
        System.out.println(getDisplayStringCenter(cardSimplified, 5));
        System.out.println(header);
    }

    @Override
    public void displayScreen() {

        String header = getHeaderString(screenWidth);

        // Prints the round name
        System.out.println(header);
        System.out.println("|" + getDisplayStringCenter(roundName, screenWidth) + "|");
        System.out.println(header);

        // Prints round description + score (on left hand side) and Current Table Text (on right
        // hand side)
        System.out.println(
                "|"
                        + getDisplayStringCenter(roundDescription, screenWidth / 8 * 3)
                        + "|"
                        + getDisplayStringCenter("Hand Chosen To Play", (screenWidth / 8 * 5) - 1)
                        + "|");

        // Get the card header for all 5 cards chosen to play
        String cardHeaders = (getHeaderString(5) + "  ").repeat(5);

        System.out.println(
                "|"
                        + getDisplayStringCenter("", screenWidth / 8 * 3)
                        + "|"
                        + getDisplayStringCenter(cardHeaders, (screenWidth / 8 * 5) - 1)
                        + "|");

        // Get the card deck here
        Card c1 = new Card(Card.Rank.ACE, Card.Suit.CLUBS);
        Card c2 = new Card(Card.Rank.TWO, Card.Suit.CLUBS);
        Card c3 = new Card(Card.Rank.THREE, Card.Suit.CLUBS);
        Card c4 = new Card(Card.Rank.FOUR, Card.Suit.CLUBS);
        Card c5 = new Card(Card.Rank.FIVE, Card.Suit.CLUBS);
        Card c6 = new Card(Card.Rank.SIX, Card.Suit.CLUBS);
        Card c7 = new Card(Card.Rank.SEVEN, Card.Suit.CLUBS);
        Card c8 = new Card(Card.Rank.EIGHT, Card.Suit.CLUBS);

        List<Card> cardsToPlay = List.of(new Card[] {c1, c2, c3, c4, c5});

        String cardValues = "";
        for (int i = 0; i < cardsToPlay.size(); i++) {
            cardValues +=
                    "|"
                            + getDisplayStringCenter(cardsToPlay.get(i).getSimplified(), 5)
                            + "|"
                            + "  ";
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

        // Printing round score (left hand side) and your hand (right hand side)
        System.out.println(
                "|"
                        + getDisplayStringCenter("Round Score: " + roundScore, screenWidth / 8 * 3)
                        + "|"
                        + getDisplayStringCenter("Your Hand: ", (screenWidth / 8 * 5) - 1)
                        + "|");

        // Your hand, it can have up to 8 cards, so print 4 on first row, 4 on second row
        List<Card> holdingHand = List.of(new Card[] {c1, c2, c3, c4, c5, c6, c7, c8});

        // Get the card header for first 4 cards in holding hand (keep variable to track current
        // cards in hand)
        String cardHeaderForCardsInHand = (getHeaderString(5) + "  ").repeat(4);

        System.out.println(
                "|"
                        + getDisplayStringCenter("", screenWidth / 8 * 3)
                        + "|"
                        + getDisplayStringCenter(
                                cardHeaderForCardsInHand, (screenWidth / 8 * 5) - 1)
                        + "|");

        String cardValuesForCardsInHand = "";
        for (int i = 0; i < 4; i++) {
            cardValuesForCardsInHand +=
                    "|"
                            + getDisplayStringCenter(cardsToPlay.get(i).getSimplified(), 5)
                            + "|"
                            + "  ";
        }

        // Printing the card value itself
        System.out.println(
                "|"
                        + getDisplayStringCenter(
                                "Mult: " + multLeft + " X " + multRight, screenWidth / 8 * 3)
                        + "|"
                        + getDisplayStringCenter(
                                cardValuesForCardsInHand, (screenWidth / 8 * 5) - 1)
                        + "|");

        System.out.println(
                "|"
                        + getDisplayStringCenter(
                                "Hands: " + handsLeft + "  Discards: " + discardsLeft,
                                screenWidth / 8 * 3)
                        + "|"
                        + getDisplayStringCenter(
                                cardHeaderForCardsInHand, (screenWidth / 8 * 5) - 1)
                        + "|");

        System.out.println(
                "|"
                        + getDisplayStringCenter("Cash: $" + currentCash, screenWidth / 8 * 3)
                        + "|"
                        + getDisplayStringCenter("", (screenWidth / 8 * 5) - 1)
                        + "|");

        System.out.println(
                "|"
                        + getDisplayStringCenter(
                                "Ante: " + anteLeft + " / " + anteRight, screenWidth / 8 * 3)
                        + "|"
                        + getDisplayStringCenter(
                                cardHeaderForCardsInHand, (screenWidth / 8 * 5) - 1)
                        + "|");

        // Printing the card value itself
        System.out.println(
                "|"
                        + getDisplayStringCenter("", screenWidth / 8 * 3)
                        + "|"
                        + getDisplayStringCenter(
                                cardValuesForCardsInHand, (screenWidth / 8 * 5) - 1)
                        + "|");

        System.out.println(
                "|"
                        + getDisplayStringCenter(
                                "Current Round: " + currentRoundNumber, screenWidth / 8 * 3)
                        + "|"
                        + getDisplayStringCenter(
                                cardHeaderForCardsInHand, (screenWidth / 8 * 5) - 1)
                        + "|");

        // Print end header
        System.out.println(header);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (Objects.equals(evt.getPropertyName(), "roundName")) {
            roundName = evt.getNewValue().toString();
        } else if (Objects.equals(evt.getPropertyName(), "remainingPlays")) {
            handsLeft = (Integer) evt.getNewValue();
        } else if (Objects.equals(evt.getPropertyName(), "remainingDiscards")) {
            discardsLeft = (Integer) evt.getNewValue();
        } else if (Objects.equals(evt.getPropertyName(), "roundName")) {
            roundName = evt.getNewValue().toString();
        } else if (Objects.equals(evt.getPropertyName(), "roundDescription")) {
            roundDescription = evt.getNewValue().toString();
        }
    }
}
