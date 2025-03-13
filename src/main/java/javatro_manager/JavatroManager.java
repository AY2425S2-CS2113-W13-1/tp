package javatro_manager;

import javatro_view.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

// Main Controller (Manager) Class
public class JavatroManager implements PropertyChangeListener {

    private static JavatroView jv; // Main View

    private static int userInput;
    private static Screen gameScreen = new GameScreen();
    private static Screen optionScreen = new OptionScreen();
    private static Screen startScreen = new StartScreen();

    public JavatroManager(JavatroView jv) {
        JavatroManager.jv = jv;
        jv.addPropertyChangeListener(this); // Register as an observer
    }

    // Changes the screen to display
    public static void setScreen(Screen destinationScreen) {
        jv.setCurrentScreen(destinationScreen);
    }

    public static Screen getGameScreen() {
        return gameScreen;
    }

    public static Screen getOptionScreen() {
        return optionScreen;
    }

    public static Screen getStartScreen() {
        return startScreen;
    }

    // Called when the view says there is a change
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("userInput")) {
            // Execute the respective command
            jv.getCurrentScreen().getCommand((int) evt.getNewValue() - 1).execute();
        }
    }
}
