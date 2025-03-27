package javatro.display;

import java.util.List;

import javatro.core.JavatroException;
import javatro.display.screens.DeckSelectScreen;
import javatro.display.screens.DiscardScreen;
import javatro.display.screens.GameScreen;
import javatro.display.screens.HelpScreen;
import javatro.display.screens.PlayScreen;
import javatro.display.screens.Screen;
import javatro.display.screens.StartScreen;

/**
 * The {@code display} class is responsible for managing and displaying different screens in the
 * application. It handles user input, manages screen transitions, and notifies observers of user
 * actions. This class follows the Singleton pattern to ensure a single instance manages all
 * display-related operations.
 */
public class UI {

    /**
     * Fixed width for the bordered message display.
     */
    public static final int BORDER_WIDTH = 100;
    /**
     * display-related constants for display formatting.
     */
    public static final String CARD = "\uD83C\uDCCF";
    public static final String HEARTS = "♥️";
    public static final String SPADES = "♠️";
    public static final String DIAMONDS = "♦️";
    public static final String CLUBS = "♣️";
    public static final String WARNING = "⚠️ ";
    public static final String WRITE = "✍️ ";
    public static final String ARROW = "╰┈➤ ";
    public static final String END = "\033[0m";

    // region FORMATTING STRINGS
    public static final String BOLD = "\033[1m";
    public static final String ITALICS = "\033[3m";
    public static final String UNDERLINE = "\033[4m";
    public static final String RED = "\033[91m";
    public static final String GREEN = "\033[92m";
    public static final String YELLOW = "\033[93m";
    public static final String BLUE = "\033[94m";
    public static final String PURPLE = "\033[35m";
    public static final String CYAN = "\033[36m";
    public static final String ORANGE = "\033[38;2;255;165;0m";
    public static final String BLACK = "\033[30m";
    public static final String WHITE_B = "\033[107m";
    public static final String BLACK_B = "\033[40m";
    /**
     * Custom border characters
     */
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
    /**
     * Predefined game-related screens.
     */
    private static final GameScreen GAME_SCREEN;
    private static final DiscardScreen DISCARD_SCREEN;
    private static final PlayScreen PLAY_SCREEN;
    private static final HelpScreen HELP_SCREEN;
    private static final StartScreen START_SCREEN;
    private static final DeckSelectScreen DECK_SELECT_SCREEN;
    /**
     * Parser instance for handling user input.
     */
    private static final Parser PARSER = new Parser();
    /**
     * The current screen being displayed to the user.
     */
    private static Screen currentScreen;

    static {
        try {
            GAME_SCREEN = new GameScreen();
            DISCARD_SCREEN = new DiscardScreen();
            PLAY_SCREEN = new PlayScreen();
            HELP_SCREEN = new HelpScreen();
            START_SCREEN = new StartScreen();
            DECK_SELECT_SCREEN = new DeckSelectScreen();
        } catch (JavatroException e) {
            System.err.println("Failed to initialize screens: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize screens", e);
        }
    }

    // endregion

    public static void printBlackB(String input) {
        System.out.print(UI.BLACK_B + input + UI.END);
    }

    /**
     * Prints a bordered message or menu with a title and dynamically generated content. Uses a
     * default width of 100.
     *
     * @param title   the title of the message or menu
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
     * @param title      the title of the message or menu
     * @param content    a list of content lines
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
     * @param text  the text to center
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
                + BLACK_B
                + " ".repeat(paddingSize)
                + BLACK_B
                + text
                + BLACK_B
                + " ".repeat(paddingSize + extraPadding)
                + BLACK_B
                + VERTICAL
                + END;
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

    /**
     * Gets the current screen being displayed.
     *
     * @return the current {@link Screen}
     */
    public static Screen getCurrentScreen() {
        return currentScreen;
    }

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
        System.out.printf(
                "%s%sTransitioning to: %s%s\n",
                ORANGE, UNDERLINE, screen.getClass().getSimpleName(), END);
        currentScreen = screen;
        currentScreen.displayScreen();
        PARSER.getOptionInput(); // This will handle retries internally
    }

    /**
     * Clears the console screen. This method uses ANSI escape codes to clear the console.
     */
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
     * Gets the help screen.
     *
     * @return the {@link HelpScreen} instance
     */
    public static DeckSelectScreen getDeckSelectScreen() {
        return DECK_SELECT_SCREEN;
    }

    // endregion
}
