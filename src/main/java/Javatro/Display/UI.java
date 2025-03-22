package Javatro.Display;

import Javatro.Core.JavatroException;
import Javatro.Display.Screens.*;
import Javatro.Manager.Parser;

/**
 * The {@code Display} class is responsible for managing and displaying different screens in the
 * application. It handles user input, manages screen transitions, and notifies observers of user
 * actions. This class follows the Singleton pattern to ensure a single instance manages all
 * Display-related operations.
 */
public class UI {

    /** The current screen being displayed to the user. */
    private static Screen currentScreen;

    /** Predefined game-related screens. */
    private static final GameScreen GAME_SCREEN;

    private static final DiscardScreen DISCARD_SCREEN;
    private static final PlayScreen PLAY_SCREEN;
    private static final HelpScreen HELP_SCREEN;
    private static final StartScreen START_SCREEN;

    /** Parser instance for handling user input. */
    private static final Parser PARSER = new Parser();

    static {
        try {
            GAME_SCREEN = new GameScreen();
            DISCARD_SCREEN = new DiscardScreen();
            PLAY_SCREEN = new PlayScreen();
            HELP_SCREEN = new HelpScreen();
            START_SCREEN = new StartScreen();
        } catch (JavatroException e) {
            System.err.println("Failed to initialize screens: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize screens", e);
        }
    }

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
     * Sets the current screen and displays it.
     *
     * @param screen the screen to be displayed
     * @throws IllegalArgumentException if the provided screen is {@code null}
     */
    public void setCurrentScreen(Screen screen) throws JavatroException {
        if (screen == null) {
            throw JavatroException.invalidScreen();
        }
        System.out.println("Transitioning to: " + screen.getClass().getSimpleName());
        currentScreen = screen;
        currentScreen.displayScreen();
        PARSER.getInput(); // This will handle retries internally
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
     * Gets the parser instance for handling user input.
     *
     * @return the {@link Parser} instance
     */
    public static Parser getParser() {
        return PARSER;
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

    /** Clears the console screen. This method uses ANSI escape codes to clear the console. */
    public static void clearScreen() {
        final String FLUSH = "\033[H\033[2J";
        System.out.print(FLUSH);
        System.out.flush(); // Ensure the output is flushed
    }
}
