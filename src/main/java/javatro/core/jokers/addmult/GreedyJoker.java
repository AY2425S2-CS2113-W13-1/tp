package javatro.core.jokers.addmult;

import static javatro.display.UI.BLACK_B;
import static javatro.display.UI.BOLD;
import static javatro.display.UI.END;
import static javatro.display.UI.ORANGE;
import static javatro.display.UI.RED;

import javatro.core.Card;
import javatro.core.Score;
import javatro.core.jokers.Joker;

/** Represents a GreedyJoker Joker. */
public class GreedyJoker extends Joker {

    public GreedyJoker() {
        super();
        name = "Greedy";
        description =
                String.format(
                        "Played cards with %s%sDiamond%s%s suit give %s%s+3 Mult%s%s when scored",
                        ORANGE, BOLD, END, BLACK_B, RED, BOLD, END, BLACK_B);
        path = "joker_greedy.txt";
        this.scoreType = ScoreType.ONCARDPLAY;
    }

    @Override
    public void interact(Score scoreClass, Card playedCard) {
        if (playedCard.suit() == Card.Suit.DIAMONDS) {
            scoreClass.totalMultiplier += 3;
        }
    }

    @Override
    public String toString() {
        return "+3 Mult for Diamond";
    }
}
