package javatro.manager.options;

import static javatro.display.UI.*;

import javatro.core.*;
import javatro.display.Parser;
import javatro.display.UI;
import javatro.display.screens.DiscardCardScreen;
import javatro.display.screens.PlayCardScreen;
import javatro.manager.JavatroManager;

import java.util.List;

/**
 * The {@code CardSelectOption} class represents a command that allows the player to make a
 * selection of cards to either play or discard, based on the current game screen.
 */
public class CardSelectOption implements Option {

    /** The default selection limit for cards. */
    public static final int DEFAULT_SELECTION_LIMIT = 5;

    /** The maximum number of cards that can be selected. */
    private final int selectionLimit;

    /** Constructs a {@code CardSelectOption} with the default selection limit of 5. */
    public CardSelectOption() throws JavatroException {
        this(DEFAULT_SELECTION_LIMIT);
    }

    /**
     * Constructs a {@code CardSelectOption} with a specified selection limit.
     *
     * @param selectionLimit The maximum number of cards that can be selected.
     * @throws JavatroException if the selection limit is invalid (less than 1).
     */
    public CardSelectOption(int selectionLimit) throws JavatroException {
        if (selectionLimit < 1) {
            throw JavatroException.invalidSelectionLimit();
        }
        this.selectionLimit = selectionLimit;
    }

    /**
     * Provides a brief description of the command.
     *
     * @return A string describing the command.
     */
    @Override
    public String getDescription() {
        return "Select Cards";
    }

    /**
     * Executes the selection command, prompting the player to select cards to either play or
     * discard. The behavior changes based on the current screen.
     *
     * @throws JavatroException if an error occurs during card selection.
     */
    @Override
    public void execute() throws JavatroException {
        List<Integer> userInput =
                Parser.getCardInput(
                        JavatroCore.currentRound.getPlayerHand().size(), selectionLimit);

        // Select and play the chosen cards
        if (UI.getCurrentScreen() instanceof PlayCardScreen) {
            JavatroCore.currentRound.playCards(userInput);

            // Print Hand Name and Cards played
            PokerHand playedHand = JavatroCore.currentRound.getPlayedHand();
            String handName =
                    String.format(
                            "Achieved: %s%s<%s>%s%s Hand",
                            BOLD, YELLOW, playedHand.getHandName(), END, BLACK_B);
            List<String> cardArtLines = getCardArtLines(JavatroCore.currentRound.getPlayedCards());
            printBorderedContent(handName, cardArtLines);

            // Increment hand play count
            JavatroCore.currentRound.getPlayedHand().incrementPlayed();

            // Discard the selected cards
        } else if (UI.getCurrentScreen() instanceof DiscardCardScreen) {
            JavatroCore.currentRound.discardCards(userInput);

            // Print Cards discarded
            String handName = BOLD + "Cards Discarded" + END + BLACK_B;
            List<String> cardArtLines = getCardArtLines(JavatroCore.currentRound.getPlayedCards());
            printBorderedContent(handName, cardArtLines);
        }

        if (JavatroCore.currentRound.isWon()
                && JavatroCore.getAnte().getAnteCount() == 8
                && JavatroCore.getAnte().getBlind() == Ante.Blind.BOSS_BLIND) {
            // Game Winning Screen
            JavatroManager.setScreen(UI.getWinGameScreen());
        } else if (JavatroCore.currentRound.isLost()) {
            // Game Losing Screen
            JavatroManager.setScreen(UI.getLoseScreen());
        } else if (JavatroCore.currentRound.isWon()) {
            // Round Winning Screen
            JavatroManager.setScreen(UI.getWinRoundScreen());
        } else {
            // Return to the game screen after selection
            JavatroManager.setScreen(UI.getGameScreen());
        }
    }
}
