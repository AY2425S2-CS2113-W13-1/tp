// @@author Markneoneo
package javatro.display;

import javatro.core.Card;
import javatro.core.JavatroException;
import javatro.display.screens.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * The {@code UI} class is responsible for managing and displaying different screens in the
 * application. It handles user input, manages screen transitions, and notifies observers of user
 * actions. This class follows the Singleton pattern to ensure a single instance manages all
 * display-related operations.
 */
public class UI {

    /** Fixed width for the bordered message display. */
    public static final int BORDER_WIDTH = 100;

    // region ANSI FORMATTING CODES
    /** ANSI escape code to reset all formatting. */
    public static final String END = "\033[0m";
    /** ANSI escape code for bold text. */
    public static final String BOLD = "\033[1m";
    /** ANSI escape code for italic text. */
    public static final String ITALICS = "\033[3m";
    /** ANSI escape code for underlined text. */
    public static final String UNDERLINE = "\033[4m";

    // Text colors
    public static final String WHITE = "\033[97m";
    public static final String RED = "\033[91m";
    public static final String GREEN = "\033[92m";
    public static final String YELLOW = "\033[93m";
    public static final String BLUE = "\033[94m";
    public static final String PURPLE = "\033[38;2;115;14;147m";
    public static final String CYAN = "\033[96m";
    public static final String ORANGE = "\033[38;2;255;165;0m";

    // Background colors
    public static final String WHITE_B = "\033[107m";
    public static final String BLACK_B = "\033[40m";
    public static final String BLUE_B = "\033[104m";
    public static final String RED_B = "\033[41m";
    public static final String PURPLE_B = "\033[48;2;115;14;147m";
    public static final String ORANGE_B = "\033[48;2;255;165;0m";

    /** Custom border characters for UI elements */
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
    // endregion

    /** Parser instance for handling user input. */
    private static final Parser PARSER = new Parser();

    // region SCREEN INSTANCES
    /** Predefined game-related screens. */
    private static final GameScreen GAME_SCREEN;

    private static final DiscardCardScreen DISCARD_SCREEN;
    private static final PlayCardScreen PLAY_SCREEN;
    private static final HelpScreen HELP_SCREEN;
    private static final StartScreen START_SCREEN;
    private static final DeckSelectScreen DECK_SELECT_SCREEN;
    private static final DeckViewScreen DECK_VIEW_SCREEN;
    private static final PokerHandScreen POKER_SCREEN;
    private static final BlindSelectScreen BLIND_SCREEN;
    private static final WinRoundScreen WIN_ROUND_SCREEN;
    private static final WinGameScreen WIN_GAME_SCREEN;
    private static final LoseScreen LOSE_SCREEN;
    private static final RunSelectScreen RUN_SELECT_SCREEN;
    private static final JumpToRunScreen JUMP_TO_RUN_SCREEN;

    static {
        try {
            // Initialize all screen components
            GAME_SCREEN = new GameScreen();
            DISCARD_SCREEN = new DiscardCardScreen();
            PLAY_SCREEN = new PlayCardScreen();
            HELP_SCREEN = new HelpScreen();
            START_SCREEN = new StartScreen();
            DECK_VIEW_SCREEN = new DeckViewScreen();
            DECK_SELECT_SCREEN = new DeckSelectScreen();
            POKER_SCREEN = new PokerHandScreen();
            BLIND_SCREEN = new BlindSelectScreen();
            WIN_ROUND_SCREEN = new WinRoundScreen();
            WIN_GAME_SCREEN = new WinGameScreen();
            LOSE_SCREEN = new LoseScreen();
            RUN_SELECT_SCREEN = new RunSelectScreen();
            JUMP_TO_RUN_SCREEN = new JumpToRunScreen();
        } catch (JavatroException e) {
            System.err.println("Failed to initialize screens: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize screens", e);
        }
    }
    // endregion

    /** The current screen being displayed to the user. */
    private static Screen currentScreen;
    /** The screen displayed before current screen. */
    private static Screen previousScreen;

    // region OUTPUT METHODS

    /**
     * Prints text with a black background using ANSI escape codes.
     *
     * @param input The text to be printed with black background
     */
    public static void printBlackB(String input) {
        assert input != null : "Input string cannot be null";
        System.out.print(BLACK_B + input + END);
    }

    /**
     * Prints a bordered message or menu with a title and dynamically generated content.
     *
     * @param title The title of the message or menu
     * @param content A list of content lines
     */
    public static void printBorderedContent(String title, List<String> content) {
        assert title != null : "Title cannot be null";
        assert content != null : "Content list cannot be null";
        printBorderedContent(title, content, BORDER_WIDTH, BORDER_WIDTH);
    }

    /**
     * Prints a bordered message or menu with customizable widths for title and content.
     *
     * @param title The title of the message or menu
     * @param content A list of content lines
     * @param titleWidth The width of the title section
     * @param contentWidth The width of the content section
     */
    public static void printBorderedContent(
            String title, List<String> content, int titleWidth, int contentWidth) {
        assert title != null : "Title cannot be null";
        assert content != null : "Content list cannot be null";
        assert titleWidth > 0 : "Title width must be positive";
        assert contentWidth > 0 : "Content width must be positive";

        // Build all border components first for efficiency
        String topBorder =
                TOP_LEFT + String.valueOf(HORIZONTAL).repeat(BORDER_WIDTH - 2) + TOP_RIGHT;
        String middleBorder =
                T_RIGHT + String.valueOf(HORIZONTAL).repeat(BORDER_WIDTH - 2) + T_LEFT;
        String bottomBorder =
                BOTTOM_LEFT + String.valueOf(HORIZONTAL).repeat(BORDER_WIDTH - 2) + BOTTOM_RIGHT;

        // Use StringBuilder to minimize IO operations
        StringBuilder output = new StringBuilder();

        // Top border
        output.append(BLACK_B).append(topBorder).append(END).append("\n");

        // Centered title
        output.append(centerText(title, titleWidth)).append("\n");

        // Middle border
        output.append(BLACK_B).append(middleBorder).append(END).append("\n");

        // Content lines
        for (String line : content) {
            output.append(centerText(line, contentWidth)).append("\n");
        }

        // Bottom border
        output.append(BLACK_B).append(bottomBorder).append(END).append("\n");

        System.out.print(output);
    }

    /**
     * Centers text within a specified width, accounting for ANSI codes and Unicode characters.
     *
     * @param text The text to center
     * @param width The total available width
     * @return The centered text surrounded by border characters
     */
    public static String centerText(String text, int width) {
        assert text != null : "Text cannot be null";
        assert width > 0 : "Width must be positive";

        int displayLength = getDisplayLength(text);

        // Handle case where text is longer than available space
        if (width <= displayLength + 2) {
            return BLACK_B + VERTICAL + " " + text + " " + VERTICAL + END;
        }

        int paddingSize = (width - displayLength - 2) / 2;
        int extraPadding = (width - displayLength - 2) % 2;

        return String.format(
                "%s%s%s%s%s%s%s%s",
                BLACK_B,
                VERTICAL,
                " ".repeat(paddingSize),
                text,
                BLACK_B,
                " ".repeat(paddingSize + extraPadding),
                VERTICAL,
                END);
    }

    /**
     * Truncates or pads a string to exactly the specified display width.
     *
     * @param text The input text
     * @param width The desired display width
     * @return The adjusted string
     */
    public static String padToWidth(String text, int width) {
        assert text != null : "Text cannot be null";
        assert width > 0 : "Width must be positive";

        if (getDisplayLength(text) > width) {
            return text.substring(0, width);
        }
        return String.format("%-" + width + "s", text);
    }

    /**
     * Calculates the visible length of text, ignoring ANSI codes and special Unicode characters.
     *
     * @param text The text to measure
     * @return The effective display length
     */
    public static int getDisplayLength(String text) {
        assert text != null : "Text cannot be null";

        // Remove ANSI escape sequences
        String strippedText = text.replaceAll("\033\\[[;\\d]*m", "");

        double length = 0;
        for (int i = 0; i < strippedText.length(); i++) {
            char c = strippedText.charAt(i);

            // Handle special Unicode characters
            if (c == '\u200A') { // Hair space
                length += 0.3;
            } else if (c == '\u2009') { // Thin space
                length += 0.5;
            } else if (c >= '\uD800' && c <= '\uDFFF') { // Surrogate pair (emoji)
                length += 2;
                i++; // Skip next character in pair
            } else { // Regular character
                length += 1;
            }
        }
        return (int) Math.round(length);
    }

    /**
     * Generates ASCII art lines for displaying cards.
     *
     * @param holdingHand The list of cards to render
     * @return List of strings representing card art lines
     */
    public static List<String> getCardArtLines(List<Card> holdingHand) {
        assert holdingHand != null : "Card list cannot be null";
        assert !holdingHand.isEmpty() : "Card list cannot be empty";

        List<String> cardArtLines = new ArrayList<>();
        final int CARD_LINE_COUNT = 5; // Number of lines per card

        // Render all cards first
        String[][] renderedCards = new String[holdingHand.size()][CARD_LINE_COUNT];
        for (int i = 0; i < holdingHand.size(); i++) {
            renderedCards[i] = CardRenderer.renderCard(holdingHand.get(i));
        }

        // Combine lines horizontally
        for (int line = 0; line < CARD_LINE_COUNT; line++) {
            StringBuilder combinedLine = new StringBuilder();
            for (int cardIdx = 0; cardIdx < renderedCards.length; cardIdx++) {
                combinedLine.append(renderedCards[cardIdx][line]);
                if (cardIdx < renderedCards.length - 1) {
                    combinedLine.append(BLACK_B).append("  ").append(END);
                }
            }
            cardArtLines.add(combinedLine.toString());
        }
        return cardArtLines;
    }

    /**
     * Prints ANSI art from a resource file.
     *
     * @param fileName The name of the file in the ansi resources directory
     */
    public static void printANSI(String fileName) {
        assert fileName != null && !fileName.isEmpty() : "Filename cannot be null or empty";

        try (InputStream inputStream =
                UI.class.getResourceAsStream("/javatro/display/ansi/" + fileName)) {
            if (inputStream == null) {
                throw JavatroException.errorLoadingLogo(fileName);
            }
            try (Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8)) {
                System.out.println(scanner.useDelimiter("\\A").next());
            }
        } catch (IOException | JavatroException e) {
            System.err.println("Error loading ANSI art: " + e.getMessage());
            System.out.println("ANSI TEXT"); // Fallback output
        }
    }
    // endregion

    // region SCREEN MANAGEMENT

    /**
     * Transitions to a new screen and updates display.
     *
     * @param screen The screen to display
     * @throws JavatroException if the screen is null
     */
    public void setCurrentScreen(Screen screen) throws JavatroException {
        assert screen != null : "Screen cannot be null";
        if (screen == null) {
            throw JavatroException.invalidScreen();
        }

        // Update screen history
        if (currentScreen != null) {
            previousScreen = currentScreen;
        }

        System.out.printf(
                "%s%sTransitioning to: %s%s%n",
                ORANGE, UNDERLINE, screen.getClass().getSimpleName(), END);
        currentScreen = screen;
        currentScreen.displayScreen();
        PARSER.getOptionInput();
    }

    /** Clears the console using ANSI escape codes. */
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    // endregion

    // region ACCESSORS

    /**
     * @return The current active screen
     */
    public static Screen getCurrentScreen() {
        return currentScreen;
    }

    /**
     * @return The previously active screen
     */
    public static Screen getPreviousScreen() {
        return previousScreen;
    }

    /**
     * @return The input parser instance
     */
    public static Parser getParser() {
        return PARSER;
    }

    // Screen-specific accessors with corrected documentation
    public static DiscardCardScreen getDiscardScreen() {
        return DISCARD_SCREEN;
    }

    public static PlayCardScreen getPlayScreen() {
        return PLAY_SCREEN;
    }

    public static GameScreen getGameScreen() {
        return GAME_SCREEN;
    }

    public static StartScreen getStartScreen() {
        return START_SCREEN;
    }

    public static HelpScreen getHelpScreen() {
        return HELP_SCREEN;
    }

    public static PokerHandScreen getPokerHandScreen() {
        return POKER_SCREEN;
    }

    public static DeckViewScreen getDeckViewScreen() {
        return DECK_VIEW_SCREEN;
    }

    public static DeckSelectScreen getDeckSelectScreen() {
        return DECK_SELECT_SCREEN;
    }

    public static WinRoundScreen getWinRoundScreen() {
        return WIN_ROUND_SCREEN;
    }

    public static WinGameScreen getWinGameScreen() {
        return WIN_GAME_SCREEN;
    }

    public static LoseScreen getLoseScreen() {
        return LOSE_SCREEN;
    }

    public static RunSelectScreen getRunSelectScreen() {
        return RUN_SELECT_SCREEN;
    }

    public static JumpToRunScreen getJumpToRunScreen() {
        return JUMP_TO_RUN_SCREEN;
    }

    // @@author swethacool
    /**
     * @return The blind selection screen instance
     */
    public static BlindSelectScreen getBlindScreen() {
        return BLIND_SCREEN;
    }
    // @@author swethacool
    // endregion
}
