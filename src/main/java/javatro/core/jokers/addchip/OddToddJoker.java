package javatro.core.jokers.addchip;

import static javatro.display.UI.BLACK_B;
import static javatro.display.UI.BLUE;
import static javatro.display.UI.BOLD;
import static javatro.display.UI.END;
import static javatro.display.UI.YELLOW;

import javatro.core.Card;
import javatro.core.Score;
import javatro.core.jokers.Joker;

/** Represents a OddToddJoker Joker */
public class OddToddJoker extends Joker {

    public OddToddJoker() {
        super();
        name = "Odd Todd";
        description =
                String.format(
                        "Played cards with %s%sOdd%s%s rank gives %s%s+31 Chips%s%s when scored (A,"
                                + " 9, 7, 5, 3)",
                        YELLOW, BOLD, END, BLACK_B, BLUE, BOLD, END, BLACK_B);
        path = "joker_odd_todd.txt";
        this.scoreType = ScoreType.ONCARDPLAY;
    }

    @Override
    public void interact(Score scoreClass, Card playedCard) {
        if ((playedCard.getChips()) % 2 != 0) {
            scoreClass.totalChips += 31;
        }
    }

    @Override
    public String toString() {
        return "+31 Chips for Odd";
    }
}
