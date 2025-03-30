package javatro.display.screens;

import static javatro.display.UI.*;

import javatro.core.*;
import javatro.core.jokers.Joker;
import javatro.core.jokers.JokerFactory;
import javatro.display.UI;
import javatro.manager.options.ExitGameOption;
import javatro.manager.options.NextRoundOption;

import java.util.List;
import java.util.Random;

public class WinRoundScreen extends Screen {

    /**
     * Constructs a screen with the specified options title.
     *
     * @throws JavatroException if the options title is null or empty
     */
    public WinRoundScreen() throws JavatroException {
        super("\\" + GREEN + " You beat the Blind! " + END + BLACK_B + "/");
        commandMap.add(new NextRoundOption());
        commandMap.add(new ExitGameOption());
    }

    /**
     * Displays the screen content. This method must be implemented by subclasses to define the
     * specific behavior and layout of the screen.
     */
    @Override
    public void displayScreen() {

        // Give the player a random joker card if boss blind is beaten
        if (JavatroCore.getAnte().getBlind() == Ante.Blind.BOSS_BLIND) {
            Joker randomJoker = JokerFactory.createRandomJoker();
            // Print Joker Card
            printANSI(randomJoker.getPath());
            String title =
                    "You got a "
                            + CYAN
                            + BOLD
                            + randomJoker.getName()
                            + END
                            + BLACK_B
                            + " Joker card!";

            try {
                JavatroCore.heldJokers.add(randomJoker);
                UI.printBorderedContent(
                        title, List.of(">> " + randomJoker.getDescription() + " <<"));
            } catch (JavatroException e) {
                UI.printBorderedContent(title, List.of("Limit for Jokers has been reached!"));
            }

            // Give the player a random free planet card if normal blinds are beaten
        } else {
            PokerHand.HandType[] handTypes = PokerHand.HandType.values();
            Random random = new Random();
            PokerHand.HandType randomPlanetCard = handTypes[random.nextInt(handTypes.length)];
            PlanetCard.getForHand(randomPlanetCard).apply();
            // Print Planet Card
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
            UI.printBorderedContent(title, List.of(lines));
        }
    }
}
