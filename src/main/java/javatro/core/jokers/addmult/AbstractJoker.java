package javatro.core.jokers.addmult;

import static javatro.display.UI.BLACK_B;
import static javatro.display.UI.BOLD;
import static javatro.display.UI.END;
import static javatro.display.UI.RED;
import static javatro.display.UI.YELLOW;

import javatro.core.Card;
import javatro.core.Score;
import javatro.core.jokers.Joker;

/** Represents a AbstractJoker Joker, based off the Abstract Joker of Balatro. */
public class AbstractJoker extends Joker {
    private int numberOfJokers;

    public AbstractJoker() {
        super();
        name = "Abstract";
        this.numberOfJokers = 1;
        description =
                String.format(
                        "%s%s+3 Mult%s%s for each %s%sJoker%s%s card held (Currently %s%s+%d%s%s"
                                + " Mult)",
                        RED,
                        BOLD,
                        END,
                        BLACK_B,
                        YELLOW,
                        BOLD,
                        END,
                        BLACK_B,
                        RED,
                        BOLD,
                        numberOfJokers * 3,
                        END,
                        BLACK_B);
        path = "joker_abstract.txt";
        this.scoreType = ScoreType.AFTERHANDPLAY;
    }

    @Override
    public void interact(Score scoreClass, Card playedCard) {
        this.numberOfJokers = scoreClass.jokerList.size();
        description =
                String.format(
                        "%s%s+3 Mult%s%s for each %s%sJoker%s%s card held (Currently %s%s+%d"
                                + " Mult%s%s)",
                        RED,
                        BOLD,
                        END,
                        BLACK_B,
                        YELLOW,
                        BOLD,
                        END,
                        BLACK_B,
                        RED,
                        BOLD,
                        numberOfJokers * 3,
                        END,
                        BLACK_B);
        scoreClass.totalMultiplier += numberOfJokers * 3;
    }

    @Override
    public String toString() {
        return "+3 Mult per Joker";
    }
}
