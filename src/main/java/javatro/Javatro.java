package javatro;

import javatro.core.JavatroCore;
import javatro.core.JavatroException;
import javatro.display.UI;
import javatro.display.screens.StartScreen;
import javatro.manager.JavatroManager;

/**
 * The {@code javatro} class serves as the main entry point for the application. It initializes the
 * necessary components, including the view, core logic, and manager, and sets the screen of the
 * manager to the start screen.
 */
public class Javatro {

    /** The view component of the application. */
    private static final UI javatroView = new UI();

    /** The core logic component of the application. */
    private static final JavatroCore javatroCore = new JavatroCore();

    /** The manager responsible for handling interactions between the view and core components. */
    private static final JavatroManager javatroManager;

    static {
        try {
            javatroManager = new JavatroManager(javatroView, javatroCore);
        } catch (JavatroException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The main entry point of the application. It initializes the start screen.
     *
     * @param args Command-line arguments (not used in this application).
     */
    public static void main(String[] args) throws JavatroException {
        assert false : "dummy assertion set to fail";
        JavatroManager.setScreen(new StartScreen());
    }
}
