// @@author Markneoneo
package javatro.display.screens;

import static javatro.display.UI.BLACK_B;
import static javatro.display.UI.BLUE;
import static javatro.display.UI.BOLD;
import static javatro.display.UI.CYAN;
import static javatro.display.UI.END;
import static javatro.display.UI.GREEN;
import static javatro.display.UI.RED;
import static javatro.display.UI.YELLOW;
import static javatro.display.UI.printANSI;
import static javatro.display.UI.printBorderedContent;

import javatro.core.Ante;
import javatro.core.JavatroCore;
import javatro.core.JavatroException;
import javatro.core.PlanetCard;
import javatro.core.PokerHand;
import javatro.core.jokers.Joker;
import javatro.core.jokers.JokerFactory;
import javatro.manager.options.ExitGameOption;
import javatro.manager.options.NextRoundOption;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Screen displayed after winning a game round, showing earned rewards.
 *
 * <p>Handles:
 *
 * <ul>
 *   <li>Joker card rewards for boss blind victories
 *   <li>Planet card rewards for regular blind victories
 *   <li>Progression system display
 * </ul>
 */
public class WinRoundScreen extends Screen {

    /**
     * Constructs round victory screen with progression options.
     *
     * @throws JavatroException if screen initialization fails
     */
    public WinRoundScreen() throws JavatroException {
        super("\\" + GREEN + " You beat the Blind! " + END + BLACK_B + "/");
        commandMap.add(new NextRoundOption());
        commandMap.add(new ExitGameOption());
    }

    /**
     * Displays round victory content based on blind type:
     *
     * <ul>
     *   <li>Boss Blind: Awards random joker with special ability
     *   <li>Normal Blind: Awards planet card with stat boosts
     * </ul>
     */
    @Override
    public void displayScreen() {
        if (JavatroCore.getAnte().getBlind() == Ante.Blind.BOSS_BLIND) {
            handleBossBlindVictory();
        } else {
            handleNormalBlindVictory();
        }
    }

    /** Handles boss blind victory rewards and display */
    private void handleBossBlindVictory() {
        Joker randomJoker = JokerFactory.createRandomJoker();
        assert randomJoker != null : "Joker factory returned null";

        printANSI(randomJoker.getPath());
        String title =
                "You got a " + CYAN + BOLD + randomJoker.getName() + END + BLACK_B + " Joker card!";

        try {
            JavatroCore.heldJokers.add(randomJoker);
            printBorderedContent(title, List.of(">> " + randomJoker.getDescription() + " <<"));
        } catch (JavatroException e) {
            printBorderedContent(title, List.of("Limit for Jokers has been reached!"));
        }
    }

    /** Handles normal blind victory rewards and display */
    private void handleNormalBlindVictory() {
        PokerHand.HandType[] handTypes = PokerHand.HandType.values();
        int randomIndex = ThreadLocalRandom.current().nextInt(handTypes.length);
        PokerHand.HandType randomPlanetCard = handTypes[randomIndex];

        assert randomIndex >= 0 && randomIndex < handTypes.length : "Invalid hand type index";

        PlanetCard.getForHand(randomPlanetCard).apply();
        printANSI(PlanetCard.getForHand(randomPlanetCard).getPath());

        String title =
                "You got a "
                        + BLUE
                        + BOLD
                        + PlanetCard.getForHand(randomPlanetCard).getName()
                        + END
                        + BLACK_B
                        + " Planet card!";

        String[] lines = {
            String.format(
                    "Poker Hand %s%s<%s>%s%s Leveled Up!",
                    BOLD, YELLOW, randomPlanetCard.getHandName(), END, BLACK_B),
            String.format(
                    "Level %s: %s%s+%d Chips%s%s and %s%s+%d Mult%s%s",
                    PlanetCard.getLevel(randomPlanetCard),
                    BOLD,
                    BLUE,
                    PlanetCard.getChipIncrement(randomPlanetCard),
                    END,
                    BLACK_B,
                    BOLD,
                    RED,
                    PlanetCard.getMultiIncrement(randomPlanetCard),
                    END,
                    BLACK_B)
        };
        printBorderedContent(title, List.of(lines));
    }
}
