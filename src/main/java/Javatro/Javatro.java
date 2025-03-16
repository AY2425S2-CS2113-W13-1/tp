package Javatro;

import javatro_core.JavatroCore;

import javatro_manager.JavatroManager;

import javatro_view.JavatroView;
import javatro_view.StartScreen;

import java.io.IOException;

/** The main class for the Javatro game. This class runs the game. */
// Primary view class that handles current view state etc
public class Javatro {
    private static final JavatroView javatroView = new JavatroView(); // View
    private static final JavatroCore javatroCore = new JavatroCore();

    private static final JavatroManager javatroManager =
            new JavatroManager(javatroView, javatroCore);

    public static void main(String[] args) throws IOException {
        JavatroManager.setScreen(new StartScreen());
        Deck deck = new Deck();
        deck.showDeck();
        Command cmd = new Command(deck);
        cmd.startNewGame();
        cmd.showDeck();

        System.out.println("\n\n");
        deck.showDeck();
    }
}
