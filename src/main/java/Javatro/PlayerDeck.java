package Javatro;

import java.util.ArrayList;

public class PlayerDeck {
    private static ArrayList<Card> playerDeck;

    public PlayerDeck(Deck deck) {
        playerDeck = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            playerDeck.add(deck.draw());
        }
    }

    public ArrayList<Card> getPlayerDeck() {
        return playerDeck;
    }

    public void showDeck() {
        for (Card card : playerDeck) {
            System.out.println(card);
        }
    }
}
