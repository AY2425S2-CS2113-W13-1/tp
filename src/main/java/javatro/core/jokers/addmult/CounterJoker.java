package javatro.core.jokers.addmult;

import javatro.core.Card;
import javatro.core.Score;
import javatro.core.jokers.Joker;

/** Represents a CounterJoker Joker, based off the Abstract Joker of Balatro. */
public class CounterJoker extends Joker {
    private int numberOfJokers;

    public CounterJoker() {
        super();
        name = "Counter";
        this.numberOfJokers = 1;
        description = "+3 Mult for each Joker card held." + "Current Multiplier: " + numberOfJokers;
        this.scoreType = ScoreType.AFTERHANDPLAY;
    }

    @Override
    public void interact(Score scoreClass, Card playedCard) {
        this.numberOfJokers = scoreClass.jokerList.size();
        description = "+3 Mult for each Joker card held." + "Current Multiplier: " + numberOfJokers;
        scoreClass.totalMultiplier += numberOfJokers * 3;
    }

    @Override
    public String toString() {
        return "+3 Mult per Joker";
    }
}
