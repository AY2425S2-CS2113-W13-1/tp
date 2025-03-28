package javatro.display;

import javatro.core.JavatroException;
import javatro.display.screens.*;

import java.util.List;

/**
 * The {@code display} class is responsible for managing and displaying different screens in the
 * application. It handles user input, manages screen transitions, and notifies observers of user
 * actions. This class follows the Singleton pattern to ensure a single instance manages all
 * display-related operations.
 */
public class UI {

    /** The current screen being displayed to the user. */
    private static Screen currentScreen;

    private static Screen previousScreen;

    /** Predefined game-related screens. */
    private static final GameScreen GAME_SCREEN;

    private static final DiscardScreen DISCARD_SCREEN;
    private static final PlayScreen PLAY_SCREEN;
    private static final HelpScreen HELP_SCREEN;
    private static final StartScreen START_SCREEN;
    private static final PokerHandScreen POKER_SCREEN;
    private static final DeckScreen DECK_SCREEN;

    /** Parser instance for handling user input. */
    private static final Parser PARSER = new Parser();

    /** Fixed width for the bordered message display. */
    public static final int BORDER_WIDTH = 100;

    static {
        try {
            GAME_SCREEN = new GameScreen();
            DISCARD_SCREEN = new DiscardScreen();
            PLAY_SCREEN = new PlayScreen();
            HELP_SCREEN = new HelpScreen();
            START_SCREEN = new StartScreen();
            POKER_SCREEN = new PokerHandScreen();
            DECK_SCREEN = new DeckScreen();
        } catch (JavatroException e) {
            System.err.println("Failed to initialize screens: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize screens", e);
        }
    }

    // region FORMATTING STRINGS
    /** display-related constants for display formatting. */
    public static final String CARD = "\uD83C\uDCCF";

    public static final String HEARTS = "♥️";
    public static final String SPADES = "♠️";
    public static final String DIAMONDS = "♦️";
    public static final String CLUBS = "♣️";
    public static final String WARNING = "⚠️ ";
    public static final String WRITE = "✍️ ";
    public static final String ARROW = "╰┈➤ ";

    public static final String END = "\033[0m";

    public static final String BOLD = "\033[1m";
    public static final String ITALICS = "\033[3m";
    public static final String UNDERLINE = "\033[4m";

    public static final String WHITE = "\033[97m";
    public static final String RED = "\033[91m";
    public static final String GREEN = "\033[92m";
    public static final String YELLOW = "\033[93m";
    public static final String BLUE = "\033[94m";
    //    public static final String PURPLE = "\033[95m";
    public static final String PURPLE = "\033[38;2;115;14;147m";
    public static final String CYAN = "\033[36m";
    public static final String ORANGE = "\033[38;2;255;165;0m";
    public static final String BLACK = "\033[30m";
    public static final String WHITE_B = "\033[107m";
    public static final String BLACK_B = "\033[40m";
    public static final String BLUE_B = "\033[104m";
    public static final String RED_B = "\033[41m";
    public static final String PURPLE_B = "\033[48;2;115;14;147m";
    public static final String ORANGE_B = "\033[48;2;255;165;0m";

    /** Custom border characters */
    public static final char TOP_LEFT = '╔';

    public static final char TOP_RIGHT = '╗';
    public static final char BOTTOM_LEFT = '╚';
    public static final char BOTTOM_RIGHT = '╝';
    public static final char HORIZONTAL = '═';
    public static final char VERTICAL = '║';
    public static final char CROSS = '╬';
    public static final char T_UP = '╩';
    public static final char T_DOWN = '╦';
    public static final char T_LEFT = '╣';
    public static final char T_RIGHT = '╠';

    // Unicode spacing characters for experimentation
    public static final String NORMAL_SPACE = " "; // U+0020
    public static final String EN_SPACE = " "; // U+2002 (1.5× normal space)
    public static final String EM_SPACE = " "; // U+2003 (2× normal space)
    public static final String THIN_SPACE = " "; // U+2009 (~½ normal space)
    public static final String HAIR_SPACE = " "; // U+200A (~⅓ normal space)
    public static final String ZERO_WIDTH_SPACE = "​"; // U+200B (invisible)
    public static final String ZERO_WIDTH_JOINER = "‍"; // U+200D
    public static final String ZERO_WIDTH_NON_JOINER = "‌"; // U+200C

    // endregion

    // region PRINTING FUNCTIONS
    public static void printBlackB(String input) {
        System.out.print(UI.BLACK_B + input + UI.END);
    }

    /**
     * Prints a bordered message or menu with a title and dynamically generated content. Uses a
     * default width of 100.
     *
     * @param title the title of the message or menu
     * @param content a list of content lines
     */
    public static void printBorderedContent(String title, List<String> content) {
        printBorderedContent(
                title,
                content,
                BORDER_WIDTH,
                BORDER_WIDTH); // Calls the main method with default width
    }

    /**
     * Prints a bordered message or menu with a title and dynamically generated content.
     *
     * @param title the title of the message or menu
     * @param content a list of content lines
     * @param titleWidth the width of the bordered content title
     */
    public static void printBorderedContent(
            String title, List<String> content, int titleWidth, int contentWidth) {

        // Top border
        printBlackB(TOP_LEFT + String.valueOf(HORIZONTAL).repeat(BORDER_WIDTH - 2) + TOP_RIGHT);
        System.out.println();

        // Centered title
        printBlackB(centerText(title, titleWidth));
        System.out.println();

        // Middle border
        printBlackB(T_RIGHT + String.valueOf(HORIZONTAL).repeat(BORDER_WIDTH - 2) + T_LEFT);
        System.out.println();

        // Display content (lines from the provider)
        for (String line : content) {
            printBlackB(centerText(line, contentWidth));
            System.out.println();
        }

        // Bottom border
        printBlackB(
                BOTTOM_LEFT + String.valueOf(HORIZONTAL).repeat(BORDER_WIDTH - 2) + BOTTOM_RIGHT);
        System.out.println();
    }

    /**
     * Centers the given text within a specified width, padding it with spaces on both sides. This
     * version handles ANSI escape codes and Unicode characters correctly.
     *
     * @param text the text to center
     * @param width the total width to center within
     * @return the centered text surrounded by borders
     */
    public static String centerText(String text, int width) {
        // Calculate display length accounting for ANSI codes and Unicode characters
        int displayLength = getDisplayLength(text);

        // Ensure width is sufficient
        if (width <= displayLength + 2) {
            return BLACK_B + VERTICAL + " " + text + " " + VERTICAL + END;
        }

        // Calculate padding
        int paddingSize = (width - displayLength - 2) / 2;
        int extraPadding = (width - displayLength - 2) % 2; // Handles odd width cases

        // Format the centered text with borders
        return BLACK_B
                + VERTICAL
                + " ".repeat(paddingSize)
                + text
                + BLACK_B
                + " ".repeat(paddingSize + extraPadding)
                + VERTICAL
                + END;
    }

    // Helper: pad or truncate a string to exactly 'width' characters.
    public static String padToWidth(String text, int width) {
        if (getDisplayLength(text) > width) {
            return text.substring(0, width);
        } else {
            return String.format("%-" + width + "s", text);
        }
    }

    // Helper method to pad a string to the right to a specified length.
    public static String padRight(String text, int width) {
        if (getDisplayLength(text) >= width) return text.substring(0, width);
        return String.format("%-" + width + "s", text);
    }

    /**
     * Calculates the visible display length of text, ignoring ANSI codes and accounting for special
     * Unicode characters.
     *
     * @param text the text to measure
     * @return the visible length of the text
     */
    private static int getDisplayLength(String text) {
        // Remove ANSI escape codes
        String strippedText = text.replaceAll("\033\\[[;\\d]*m", "");

        // Calculate adjusted length accounting for special characters
        double length = 0;
        for (int i = 0; i < strippedText.length(); i++) {
            char c = strippedText.charAt(i);
            if (c == '\u200A') { // Hair space
                length += 0.3;
            } else if (c == '\u2009') { // Thin space
                length += 0.5;
            } else if (c == '\u2002') { // En space
                length += 1.5;
            } else if (c == '\u2003') { // Em space
                length += 2;
            } else if (c == '\u200B' || c == '\u200C' || c == '\u200D') { // Zero-width
                // No addition to length
            } else if (c >= '\uD800' && c <= '\uDFFF') { // Surrogate pairs (emoji)
                length += 2;
                i++; // Skip the next char in the pair
            } else {
                length += 1;
            }
        }

        return (int) Math.round(length);
    }

    // endregion

    /**
     * Sets the current screen and displays it.
     *
     * @param screen the screen to be displayed
     * @throws IllegalArgumentException if the provided screen is {@code null}
     */
    public void setCurrentScreen(Screen screen) throws JavatroException {
        if (screen == null) {
            throw JavatroException.invalidScreen();
        }

        // Store the current screen as previous before changing
        if (currentScreen != null) {
            previousScreen = currentScreen;
        }

        System.out.printf(
                "%s%sTransitioning to: %s%s\n",
                ORANGE, UNDERLINE, screen.getClass().getSimpleName(), END);
        currentScreen = screen;
        currentScreen.displayScreen();
        PARSER.getOptionInput(); // This will handle retries internally
    }

    /**
     * Gets the current screen being displayed.
     *
     * @return the current {@link Screen}
     */
    public static Screen getCurrentScreen() {
        return currentScreen;
    }

    /**
     * Gets the previous screen that was displayed before the current one.
     *
     * @return the previous {@link Screen}, or null if there wasn't one
     */
    public static Screen getPreviousScreen() {
        return previousScreen;
    }

    /**
     * Navigates back to the previous screen if one exists.
     *
     * @throws JavatroException if there is no previous screen to return to
     */
    public void returnScreen() throws JavatroException {
        if (previousScreen == null) {
            throw new JavatroException("No previous screen to return to");
        }
        setCurrentScreen(previousScreen);
    }

    /** Clears the console screen. This method uses ANSI escape codes to clear the console. */
    public static void clearScreen() {
        final String FLUSH = "\033[H\033[2J";
        System.out.print(FLUSH);
        System.out.flush(); // Ensure the output is flushed
    }

    /**
     * Gets the parser instance for handling user input.
     *
     * @return the {@link Parser} instance
     */
    public static Parser getParser() {
        return PARSER;
    }

    // region Screen Getters
    /**
     * Gets the screen where users select cards to discard.
     *
     * @return the {@link DiscardScreen} instance
     */
    public static DiscardScreen getDiscardScreen() {
        return DISCARD_SCREEN;
    }

    /**
     * Gets the screen where users select cards to play.
     *
     * @return the {@link PlayScreen} instance
     */
    public static PlayScreen getPlayScreen() {
        return PLAY_SCREEN;
    }

    /**
     * Gets the game screen.
     *
     * @return the {@link GameScreen} instance
     */
    public static GameScreen getGameScreen() {
        return GAME_SCREEN;
    }

    /**
     * Gets the start screen.
     *
     * @return the {@link StartScreen} instance
     */
    public static StartScreen getStartScreen() {
        return START_SCREEN;
    }

    /**
     * Gets the help screen.
     *
     * @return the {@link HelpScreen} instance
     */
    public static HelpScreen getHelpScreen() {
        return HELP_SCREEN;
    }

    /**
     * Gets the poker hand screen.
     *
     * @return the {@link PokerHandScreen} instance
     */
    public static PokerHandScreen getPokerHandScreen() {
        return POKER_SCREEN;
    }

    /**
     * Gets the Deck screen.
     *
     * @return the {@link DeckScreen} instance
     */
    public static DeckScreen getDeckScreen() {
        return DECK_SCREEN;
    }
    // endregion
}
