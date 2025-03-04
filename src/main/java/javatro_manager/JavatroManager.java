package javatro_manager;

import javatro_view.GameScreen;
import javatro_view.JavatroView;
import javatro_view.Screen;
import javatro_view.StartScreen;

import java.util.Scanner;

// Main Controller (Manager) Class
public class JavatroManager {

    private static JavatroView jv;
    private static LoadStartScreenCommand loadStartScreenCommand;
    private static LoadGameScreenCommand loadGameScreenCommand;
    private static int userInput;

    public JavatroManager(JavatroView jv) {
        JavatroManager.jv = jv;
        loadStartScreenCommand = new LoadStartScreenCommand(new StartScreen());
        loadGameScreenCommand = new LoadGameScreenCommand(new GameScreen());
    }

    public static void setScreen(Screen destinationScreen) {
        // Changes the screen to display
        jv.setCurrentScreen(destinationScreen);
    }

    protected static void getInput() {
        Scanner scanner = new Scanner(System.in);
        int maxRange =
                jv.getCurrentScreen()
                        .getOptionsSize(); // Change this value to set a different range
        userInput = -1;

        while (true) {
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
    }

    // Starts a new game, is called at the beginning
    public void startGame() {
        loadStartScreenCommand.execute();
        if (userInput == 1) {
            loadGameScreenCommand.execute();
        }
    }
}
