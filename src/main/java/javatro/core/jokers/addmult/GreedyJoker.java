package javatro.core.jokers.addmult;

import javatro.core.Card;
import javatro.core.Score;
import javatro.core.jokers.Joker;

/** Represents a GreedyJoker Joker. */
public class GreedyJoker extends Joker {

    public GreedyJoker() {
        super();
        name = "Greedy";
        this.description = "Played cards with Diamond suit give +3 Mult when scored";
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
        return "+3 Mult Diamond";
    }
}
