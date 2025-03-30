package javatro.core.jokers.addchip;

import static javatro.display.UI.BLACK_B;
import static javatro.display.UI.BLUE;
import static javatro.display.UI.BOLD;
import static javatro.display.UI.END;
import static javatro.display.UI.YELLOW;

import javatro.core.Card;
import javatro.core.Score;
import javatro.core.jokers.Joker;

/** Represents a AbstractJoker Joker, based off the Abstract Joker of Balatro. */
public class ScaryFaceJoker extends Joker {

    public ScaryFaceJoker() {
        super();
        name = "Scary Face";
        description =
                String.format(
                        "Played %s%sFace%s%s (K, Q, J) Cards give %s%s+30 Chips%s%s",
                        YELLOW, BOLD, END, BLACK_B, BLUE, BOLD, END, BLACK_B);
        path = "joker_scary_face.txt";
        this.scoreType = ScoreType.ONCARDPLAY;
    }

    @Override
    public void interact(Score scoreClass, Card playedCard) {
        if (playedCard.rank() == Card.Rank.KING
                || playedCard.rank() == Card.Rank.QUEEN
                || playedCard.rank() == Card.Rank.JACK) {
            scoreClass.totalChips += 30;
        }
    }

    @Override
    public String toString() {
        return "+30 Chips for Face";
    }
}
