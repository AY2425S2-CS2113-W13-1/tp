package Javatro;

import javatro_manager.JavatroManager;

import javatro_view.JavatroView;
import javatro_view.StartScreen;

import java.io.IOException;

/** The main class for the Javatro game. This class runs the game. */
// Primary view class that handles current view state etc
public class Javatro {
    private static final JavatroView javatroView = new JavatroView(); // View
    private static final JavatroManager javatroManager = new JavatroManager(javatroView);

    public static void main(String[] args) throws IOException {
        JavatroManager.setScreen(new StartScreen());
    }
}
