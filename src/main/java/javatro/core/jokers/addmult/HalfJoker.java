package javatro.core.jokers.addmult;

import static javatro.display.UI.BLACK_B;
import static javatro.display.UI.BOLD;
import static javatro.display.UI.END;
import static javatro.display.UI.RED;
import static javatro.display.UI.YELLOW;

import javatro.core.Card;
import javatro.core.Score;
import javatro.core.jokers.Joker;

/** Represents a HalfJoker Joker. */
public class HalfJoker extends Joker {
    public HalfJoker() {
        super();
        name = "Half";
        description =
                String.format(
                        "%s%s+20 Mult%s%s if played hand has %s%s3 or fewer%s%s cards",
                        RED, BOLD, END, BLACK_B, YELLOW, BOLD, END, BLACK_B);
        path = "joker_half.txt";
        this.scoreType = ScoreType.AFTERHANDPLAY;
    }

    @Override
    public void interact(Score scoreClass, Card playedCard) {
        if (scoreClass.playedCardsList.size() <= 3) {
            scoreClass.totalMultiplier += 20;
        }
    }

    @Override
    public String toString() {
        return "+20 Mult for <= 3 Cards";
    }
}
