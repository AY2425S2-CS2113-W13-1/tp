package javatro.core.jokers.addmult;

import javatro.core.Card;
import javatro.core.Score;
import javatro.core.jokers.Joker;

/** Represents a GluttonousJoker Joker. */
public class GluttonousJoker extends Joker {

    public GluttonousJoker() {
        super();
        name = "Gluttonous";
        this.description = "Played cards with Club suit give +3 Mult when scored";
        this.scoreType = ScoreType.ONCARDPLAY;
    }

    @Override
    public void interact(Score scoreClass, Card playedCard) {
        if (playedCard.suit() == Card.Suit.CLUBS) {
            scoreClass.totalMultiplier += 3;
        }
    }

    @Override
    public String toString() {
        return "+3 Mult Clubs";
    }
}
