/**
 * The {@code JavatroManager} class serves as the main controller (manager) of the game,
 * coordinating interactions between the model ({@code JavatroCore}) and the view ({@code
 * UI}). It listens for property changes and updates the game state accordingly.
 */
package Javatro.Manager;

import Javatro.Core.JavatroCore;
import Javatro.Exception.JavatroException;
import Javatro.UI.Screens.Screen;
import Javatro.UI.UI;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Manages the interaction between the game model and the user interface. It listens for user inputs
 * and updates the game state dynamically.
 */
public class JavatroManager implements PropertyChangeListener {

    /** The main view responsible for rendering the user interface. */
    private static UI ui;
    /** The main model responsible for handling game logic. */
    private static JavatroCore jc;
    /** Stores the last recorded user input. */
    private static int userInput;

    /**
     * Constructs a {@code JavatroManager} and registers it as an observer to the view.
     *
     * @param ui The main view of the game.
     * @param jc The main model of the game.
     */
    public JavatroManager(UI ui, JavatroCore jc) {
        JavatroManager.ui = ui;
        JavatroManager.jc = jc;
        UI.getParser().addPropertyChangeListener(this); // Register as an observer
    }

    /**
     * Changes the currently displayed screen.
     *
     * @param destinationScreen The new screen to be displayed.
     */
    public static void setScreen(Screen destinationScreen) {
        ui.setCurrentScreen(destinationScreen);
    }

    /**
     * Begins the game by initializing the game model and registering necessary observers.
     *
     * @throws JavatroException If an error occurs during game initialization.
     */
    public static void beginGame() throws JavatroException {
        jc.beginGame();
        JavatroCore.currentRound.addPropertyChangeListener(Javatro.UI.UI.getGameScreen());
        // Fire property changes here
        JavatroCore.currentRound.updateRoundVariables();
    }

    /**
     * Handles property change events from the view. If the property change corresponds to user
     * input, it executes the appropriate command.
     *
     * @param evt The property change event.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("userInput")) {
            // Execute the respective command
            try {
                UI.getCurrentScreen().getCommand((int) evt.getNewValue() - 1).execute();
            } catch (JavatroException e) {
                System.out.println(e.getMessage());
                ui.setCurrentScreen(UI.getCurrentScreen());
                //throw new RuntimeException(e);
            }
        }
    }
}
