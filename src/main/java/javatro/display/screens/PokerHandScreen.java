package javatro.display.screens;

import static javatro.display.UI.*;

import javatro.core.JavatroException;
import javatro.core.PlanetCard;
import javatro.core.PokerHand;
import javatro.manager.options.ReturnOption;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PokerHandScreen extends Screen {

    // %-X = X characters width
    private static final String TITLE =
            String.format(
                    "%s%s%-5s     %s%-15s       %s%-5s%s × %s%-5s%s     %s%-6s%s",
                    BOLD,
                    GREEN,
                    "LEVEL",
                    WHITE,
                    "POKER HAND",
                    BLUE_B,
                    "CHIPS",
                    BLACK_B,
                    RED_B,
                    "MULTI",
                    BLACK_B,
                    ORANGE,
                    "PLAYS",
                    END);

    private final List<PokerHand> pokerHands;

    public PokerHandScreen() throws JavatroException {
        super("Javatro Poker Hands");
        super.commandMap.add(new ReturnOption());

        this.pokerHands =
                Stream.of(PokerHand.HandType.values())
                        .map(PokerHand::new)
                        .collect(Collectors.toList());
    }

    @Override
    public void displayScreen() {
        List<String> handDisplays =
                pokerHands.stream()
                        .map(
                                hand -> {
                                    PokerHand.HandType type = hand.handType();
                                    return String.format(
                                            "%s%4d       %s%-15s       %s%5d%s × %s%-5d%s      "
                                                    + " %s%-5d%s",
                                            GREEN,
                                            PlanetCard.getLevel(type),
                                            WHITE,
                                            type.getHandName(),
                                            BLUE_B,
                                            hand.getChips(),
                                            BLACK_B,
                                            RED_B,
                                            hand.getMultiplier(),
                                            BLACK_B,
                                            ORANGE,
                                            hand.playCount(),
                                            END);
                                })
                        .collect(Collectors.toList());

        printBorderedContent(TITLE, handDisplays);
    }

    /**
     * Updates the played count for a specific hand type.
     *
     * @param handType The hand type to update
     */
    public void incrementPlayed(PokerHand.HandType handType) {
        pokerHands.replaceAll(hand -> hand.handType() == handType ? hand.incrementPlayed() : hand);
    }

    /**
     * Gets a specific poker hand by type.
     *
     * @param handType The hand type to retrieve
     * @return The PokerHand instance
     */
    public PokerHand getHand(PokerHand.HandType handType) throws JavatroException {

        if (handType == null) {
            throw new JavatroException("Invalid Hand Type");
        }

        return pokerHands.stream()
                .filter(hand -> hand.handType() == handType)
                .findFirst()
                .orElseThrow();
    }

    public List<PokerHand> getPokerHands() {
        return pokerHands;
    }
}
