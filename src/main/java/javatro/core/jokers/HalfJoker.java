package javatro.core.jokers;

import javatro.core.Card;
import javatro.core.JavatroCore;
import javatro.core.Score;

/**
 * Represents a HalfJoker Joker.
 */
public class HalfJoker extends Joker {
    public HalfJoker() {
        super();
        this.description = "+20 Mult if played hand has 3 or fewer cards.";
        this.scoreType = ScoreType.AFTERPLAYHAND;
    }

    @Override
    public void interact (Score scoreClass, Card playedCard) {
        if (scoreClass.playedCardsList.size() <= 3) {
            scoreClass.totalMultiplier += 20;
        }
    }

}
