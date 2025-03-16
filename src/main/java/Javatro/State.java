package Javatro;

public class State {
    private int blindScore;
    private int playsPerRound;
    private Deck deck;

    public State(int blindScore, int playsPerRound, Deck deck) {
        this.blindScore = blindScore;
        this.playsPerRound = playsPerRound;
        this.deck = deck;
    }

    public int getBlindScore() {
        return blindScore;
    }

    public int getPlaysPerRound() {
        return playsPerRound;
    }

    public Deck getDeck() {
        return deck;
    }

    // Setters
    public void setBlindScore(int blindScore) {
        this.blindScore = blindScore;
    }

    public void setPlaysPerRound(int playsPerRound) {
        this.playsPerRound = playsPerRound;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }
}
