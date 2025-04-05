package javatro.core.jokers.addmult;

// @@author jwyk

import static javatro.display.UI.BLACK_B;
import static javatro.display.UI.BOLD;
import static javatro.display.UI.END;
import static javatro.display.UI.PURPLE;
import static javatro.display.UI.RED;

import javatro.core.Card;
import javatro.core.Score;
import javatro.core.jokers.Joker;

/** Represents a WrathfulJoker Joker. */
public class WrathfulJoker extends Joker {

    public WrathfulJoker() {
        super();
        name = "Wrathful";
        description =
                String.format(
                        "Played cards with %s%sSpade%s%s suit give %s%s+3 Mult%s%s when scored",
                        PURPLE, BOLD, END, BLACK_B, RED, BOLD, END, BLACK_B);
        path = "joker_wrathful.txt";
        this.scoreType = ScoreType.ONCARDPLAY;
    }

    @Override
    public void interact(Score scoreClass, Card playedCard) {
        if (playedCard.suit() == Card.Suit.SPADES) {
            scoreClass.totalMultiplier += 3;
        }
    }

    @Override
    public String toString() {
        return "+3 Mult for Spade";
    }
}
