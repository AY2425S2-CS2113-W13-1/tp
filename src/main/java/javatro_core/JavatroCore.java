package javatro_core;

import Javatro.Deck;
import Javatro.JavatroException;
import Javatro.Round;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

// Main Model class
public class JavatroCore {
    private static Round currentRound; // Instance of round class

    private PropertyChangeSupport support = new PropertyChangeSupport(this); // Observable

    // Register an observer (Controller)
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    // Start a round and assign to current round
    private void startNewRound(
            int blindScore,
            int remainingPlays,
            Deck deck,
            String roundName,
            String roundDescription)
            throws JavatroException {
        currentRound = new Round(blindScore, remainingPlays, deck, roundName, roundDescription);

        // Fire property changes here
        support.firePropertyChange("blindScore", null, blindScore);
        support.firePropertyChange("remainingPlays", null, remainingPlays);
        support.firePropertyChange("remainingDiscards", null, Round.MAX_DISCARDS_PER_ROUND);
        support.firePropertyChange("roundName", null, roundName);
        support.firePropertyChange("roundDescription", null, roundDescription);
    }

    // Called when start game is selected
    public void beginGame() throws JavatroException {
        Deck d = new Deck();
        startNewRound(1200, 10, d, "The Eye", "No repeat hands");
    }
}
