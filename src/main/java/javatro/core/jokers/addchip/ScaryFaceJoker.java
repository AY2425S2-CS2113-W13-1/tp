package javatro.core.jokers.addchip;

import javatro.core.Card;
import javatro.core.Score;
import javatro.core.jokers.Joker;

/** Represents a CounterJoker Joker, based off the Abstract Joker of Balatro. */
public class ScaryFaceJoker extends Joker {

    public ScaryFaceJoker() {
        super();
        name = "Scary Face";
        this.description = "Played Face (K, Q, J) Cards give +30 Chips";
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
        return "+3 Mult per Joker";
    }
}
