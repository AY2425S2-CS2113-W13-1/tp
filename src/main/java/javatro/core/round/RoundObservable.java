package javatro.core.round;

import javatro.core.Ante;
import javatro.core.Card;
import javatro.core.JavatroCore;
import javatro.core.JavatroException;
import javatro.core.jokers.HeldJokers;
import javatro.storage.DataParser;
import javatro.storage.Storage;
import javatro.storage.StorageManager;
import javatro.storage.utils.CardUtils;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

import static javatro.core.JavatroCore.heldJokers;

/** Handles observer notifications for round state changes without direct Round dependencies. */
class RoundObservable {
    /** The property change support for the observer pattern. */
    private final PropertyChangeSupport support;

    private final Storage storage = Storage.getStorageInstance();

    /**
     * Creates a new observable with the given source object.
     *
     * @param source The source object for property change events
     */
    public RoundObservable(Object source) {
        this.support = new PropertyChangeSupport(source);
    }

    /**
     * Registers an observer to listen for property changes.
     *
     * @param pcl The property change listener to register.
     */
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    /**
     * Fires property change events to notify observers of updated round variables.
     *
     * @param state The current state of the round
     * @param config The configuration of the round
     */
    public void updateRoundVariables(RoundState state, RoundConfig config) {
        support.firePropertyChange("blindScore", null, config.getBlindScore());
        support.firePropertyChange("remainingPlays", null, state.getRemainingPlays());
        support.firePropertyChange("remainingDiscards", null, state.getRemainingDiscards());
        support.firePropertyChange("roundName", null, config.getRoundName());
        support.firePropertyChange("roundDescription", null, config.getRoundDescription());
        support.firePropertyChange("holdingHand", null, state.getPlayerHandCards());
        support.firePropertyChange("currentScore", null, state.getCurrentScore());

        if (Storage.saveActive) {
            Storage storage = Storage.getStorageInstance();
            int runIndex = storage.getRunChosen() - 1;

            // Retrieve the run data
            ArrayList<String> runData = StorageManager.getInstance().getRunData(runIndex);
            assert runData != null : "Run data should not be null";

            // Update play counts, discards, and round score in the runData list
            runData.set(DataParser.HAND_INDEX, Integer.toString(state.getRemainingPlays()));
            runData.set(DataParser.DISCARD_INDEX, Integer.toString(state.getRemainingDiscards()));
            runData.set(DataParser.ROUND_SCORE_INDEX, Long.toString(state.getCurrentScore()));

            // Update Holding Hand Cards
            int cardIndex = DataParser.HOLDING_HAND_START_INDEX;
            for (Card c : state.getPlayerHandCards()) {
                runData.set(cardIndex, CardUtils.cardToString(c));
                cardIndex++;
            }

            // Update Rest of the Deck Cards
            ArrayList<Card> restOfTheCards = JavatroCore.currentRound.getDeck().getWholeDeck();

            for (int j = DataParser.START_OF_REST_OF_DECK;
                    j < DataParser.START_OF_REST_OF_DECK + 44;
                    j++) {
                int idx = j - DataParser.START_OF_REST_OF_DECK;
                if (idx >= restOfTheCards.size()) {
                    runData.set(j, "-");
                } else {
                    runData.set(j, CardUtils.cardToString(restOfTheCards.get(idx)));
                }
            }

            // Update Wins or Losses if applicable
            if (JavatroCore.currentRound.isRoundOver() && JavatroCore.currentRound.isWon()) {
                int currentWins = Integer.parseInt(runData.get(DataParser.WINS_INDEX));
                runData.set(DataParser.WINS_INDEX, String.valueOf(currentWins + 1));
                runData.set(DataParser.HAND_INDEX, Integer.toString(-1));
                runData.set(DataParser.DISCARD_INDEX, Integer.toString(-1));

                Ante.Blind nextBlind = JavatroCore.getAnte().getNextBlind();
                int anteCount = JavatroCore.getAnte().getAnteCount();
                if (nextBlind == Ante.Blind.SMALL_BLIND
                        && JavatroCore.getAnte().getBlind() == Ante.Blind.BOSS_BLIND) {
                    anteCount = anteCount + 1;
                }

                runData.set(DataParser.BLIND_INDEX, nextBlind.getName());
                runData.set(DataParser.ANTE_NUMBER_INDEX, String.valueOf(anteCount));
                runData.set(
                        DataParser.ROUND_NUMBER_INDEX,
                        String.valueOf(JavatroCore.getRoundCount() + 1));

                // Set all Holding Hand cards to "NA"
                for (int i = DataParser.HOLDING_HAND_START_INDEX;
                        i < DataParser.HOLDING_HAND_START_INDEX + 8;
                        i++) {
                    runData.set(i, "NA");
                }

                // Set all Rest of Deck cards to "NA"
                for (int i = DataParser.START_OF_REST_OF_DECK;
                        i < DataParser.START_OF_REST_OF_DECK + 44;
                        i++) {
                    runData.set(i, "NA");
                }

                runData.set(DataParser.ROUND_SCORE_INDEX, Long.toString(0));
            }

            if (JavatroCore.currentRound.isRoundOver() && JavatroCore.currentRound.isLost()) {
                int currentLosses = Integer.parseInt(runData.get(DataParser.LOSSES_INDEX));
                runData.set(DataParser.LOSSES_INDEX, String.valueOf(currentLosses + 1));
                runData.set(DataParser.HAND_INDEX, Integer.toString(-1));
                runData.set(DataParser.DISCARD_INDEX, Integer.toString(-1));

                // Set all Holding Hand cards to "NA"
                for (int i = DataParser.HOLDING_HAND_START_INDEX;
                        i < DataParser.HOLDING_HAND_START_INDEX + 8;
                        i++) {
                    runData.set(i, "NA");
                }

                // Set all Rest of Deck cards to "NA"
                for (int i = DataParser.START_OF_REST_OF_DECK;
                        i < DataParser.START_OF_REST_OF_DECK + 44;
                        i++) {
                    runData.set(i, "NA");
                }

                runData.set(DataParser.ROUND_SCORE_INDEX, Long.toString(0));
            }

            // Update Jokers
            heldJokers = new HeldJokers();
            for (int i = DataParser.JOKER_HAND_START_INDEX;
                 i < DataParser.JOKER_HAND_START_INDEX + 5;
                 i++) {
                String jokerString = runData.get(i);
                if (jokerString.equals("-") || jokerString.equals("NA")) {
                    continue;
                }

                try {
                    heldJokers.add(CardUtils.parseJokerString(jokerString));
                } catch (JavatroException e) {
                    throw new RuntimeException(e);
                }
            }


            // Save all updated data at once
            StorageManager.getInstance().saveRunData(runIndex, runData);

            try {
                storage.updateSaveFile();
            } catch (JavatroException e) {
                System.out.println("Failed to Save To System");
            }
        }
    }
}
