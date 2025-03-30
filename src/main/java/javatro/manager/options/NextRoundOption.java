package javatro.manager.options;

import static javatro.manager.JavatroManager.jc;

import javatro.core.Ante;
import javatro.core.JavatroCore;
import javatro.core.JavatroException;
import javatro.core.PlanetCard;
import javatro.core.PokerHand;
import javatro.core.jokers.Joker;
import javatro.core.jokers.JokerFactory;
import javatro.display.UI;
import javatro.manager.JavatroManager;

import java.util.List;
import java.util.Random;

public class NextRoundOption implements Option {
    @Override
    public String getDescription() {
        return "Start Next Round";
    }

    /**
     * Executes the command to move on to the next round. The command will give the player a {@code
     * PlanetCard} or a {@code Joker}, depending on the Blind won.
     */
    @Override
    public void execute() throws JavatroException {
        if (JavatroCore.getAnte().getBlind() == Ante.Blind.BOSS_BLIND) {
            // Give the player a random joker card, and move to next round.
            Joker randomJoker = JokerFactory.createRandomJoker();
            String title = "Won the round! Got a free " + randomJoker.getName() + " card.";
            String[] lines = {randomJoker.getName() + ": " + randomJoker.getDescription() + "."};
            try {
                JavatroCore.heldJokers.add(randomJoker);
                UI.printBorderedContent(title, List.of(lines));
            } catch (JavatroException e) {
                lines = new String[] {"Can't add any more Jokers, or limit will be exceeded."};
                UI.printBorderedContent(title, List.of(lines));
            }

        } else {
            // Give the player a random free planet card, and move to next round.
            PokerHand.HandType[] handTypes = PokerHand.HandType.values();
            Random random = new Random();
            PokerHand.HandType randomPlanetCard = handTypes[random.nextInt(handTypes.length)];
            PlanetCard.getForHand(randomPlanetCard).apply();
            String title =
                    "Won the round! Got a free "
                            + PlanetCard.getForHand(randomPlanetCard).getName()
                            + " card.";

            String[] lines = {
                randomPlanetCard.getHandName()
                        + ": +"
                        + PlanetCard.getChipIncrement(randomPlanetCard)
                        + " Chips"
                        + ", +"
                        + PlanetCard.getMultiIncrement(randomPlanetCard)
                        + " Mult.",
            };
            UI.printBorderedContent(title, List.of(lines));
        }

        // Instantiate next round
        jc.nextRound();
        JavatroCore.currentRound.addPropertyChangeListener(javatro.display.UI.getGameScreen());
        JavatroCore.currentRound.updateRoundVariables();
        javatro.display.UI.getGameScreen().restoreGameCommands();
        JavatroManager.setScreen(UI.getGameScreen());
    }
}
