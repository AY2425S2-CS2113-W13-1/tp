package javatro_view;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Scanner;

// Main View Class
public class JavatroView {

    private Screen currentScreen; // Current UI that is displayed to user
    private PropertyChangeSupport support = new PropertyChangeSupport(this); // Observable

    private static GameScreen gameScreen = new GameScreen();
    private static OptionScreen optionScreen = new OptionScreen();
    private static StartScreen startScreen = new StartScreen();

    // Register an observer (Controller)
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    // Method to load the set the currentScreen (e.g. start game, options)
    public void setCurrentScreen(Screen s) {
        currentScreen = s;
        currentScreen.displayScreen();
        getInput(); // Get user input
    }

    public Screen getCurrentScreen() {
        return currentScreen;
    }

    public static GameScreen getGameScreen() {
        return gameScreen;
    }

    public static OptionScreen getOptionScreen() {
        return optionScreen;
    }

    public static StartScreen getStartScreen() {
        return startScreen;
    }

    public static void clearConsole() {
        String FLUSH = "\033[H\033[2J";
        System.out.print(FLUSH);
        System.out.flush();
    }

    public void getInput() {
        Scanner scanner = new Scanner(System.in);
        int userInput = -1;
        int maxRange =
                getCurrentScreen().getOptionsSize(); // Change this value to set a different range
        while (true) {
            currentScreen.displayOptions();
            System.out.print("Enter a number (1 to " + maxRange + "): ");
            if (scanner.hasNextInt()) {
                userInput = scanner.nextInt();
                if (userInput >= 1 && userInput <= maxRange) {
                    break; // Valid input, exit loop
                } else {
                    System.out.println(
                            "Invalid input! Please enter a number between 1 and " + maxRange + ".");
                }
            } else {
                System.out.println("Invalid input! Please enter a number.");
                scanner.next(); // Clear invalid input
            }
        }
        // When user input is received, trigger all the observers
        support.firePropertyChange("userInput", null, userInput);
        clearConsole();
    }
}
