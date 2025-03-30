package javatro.core;

import javatro.storage.Storage;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/** Handles observer notifications for round state changes. */
public class RoundObservable {
    private final Round round;
    private final PropertyChangeSupport support;

    /**
     * Creates a new observable for the given round.
     *
     * @param round The round to observe
     */
    public RoundObservable(Round round) {
        this.round = round;
        this.support = new PropertyChangeSupport(round);
    }

    /**
     * Registers an observer to listen for property changes.
     *
     * @param pcl The property change listener to register.
     */
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    /** Fires property change events to notify observers of updated round variables. */
    public void updateRoundVariables() {
        RoundConfig config = round.getConfig();
        RoundState state = round.getState();

        support.firePropertyChange("blindScore", null, config.getBlindScore());
        support.firePropertyChange("remainingPlays", null, state.getRemainingPlays());
        support.firePropertyChange("remainingDiscards", null, state.getRemainingDiscards());
        support.firePropertyChange("roundName", null, config.getRoundName());
        support.firePropertyChange("roundDescription", null, config.getRoundDescription());
        support.firePropertyChange("holdingHand", null, round.getPlayerHand());
        support.firePropertyChange("currentScore", null, state.getCurrentScore());

        // Update Storage's playing hand
        int i = 0;
        for (Card card : round.getPlayerHand()) {
            String hand = Storage.cardToString(card);
            Storage.getInstance().getRunData().get(Storage.chosenRun).set(i + 3, hand);
            i += 1;
        }

        // Update Storage's current round
        Storage.getInstance()
                .getRunData()
                .get(Storage.chosenRun)
                .set(0, Integer.toString(JavatroCore.roundCount));

        // Update Storage's current ante
        Storage.getInstance()
                .getRunData()
                .get(Storage.chosenRun)
                .set(1, Integer.toString(JavatroCore.getAnte().getAnteCount()));

        try {
            Storage.getInstance().updateSaveFile();
        } catch (JavatroException e) {
            throw new RuntimeException(e);
        }
    }
}
