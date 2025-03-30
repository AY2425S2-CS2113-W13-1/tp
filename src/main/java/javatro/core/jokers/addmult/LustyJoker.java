package javatro.core.jokers.addmult;

import static javatro.display.UI.BLACK_B;
import static javatro.display.UI.BOLD;
import static javatro.display.UI.END;
import static javatro.display.UI.RED;

import javatro.core.Card;
import javatro.core.Score;
import javatro.core.jokers.Joker;

/** Represents a LustyJoker Joker. */
public class LustyJoker extends Joker {

    public LustyJoker() {
        super();
        name = "Lusty";
        description =
                String.format(
                        "Played cards with %s%sHeart%s%s suit give %s%s+3 Mult%s%s when scored",
                        RED, BOLD, END, BLACK_B, RED, BOLD, END, BLACK_B);
        path = "joker_lusty.txt";
        this.scoreType = ScoreType.ONCARDPLAY;
    }

    @Override
    public void interact(Score scoreClass, Card playedCard) {
        if (playedCard.suit() == Card.Suit.HEARTS) {
            scoreClass.totalMultiplier += 3;
        }
    }

    @Override
    public String toString() {
        return "+3 Mult for Hearts";
    }
}
