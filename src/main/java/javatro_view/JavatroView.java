package javatro_view;

// View Class
public class JavatroView {

    private Screen currentScreen; // Current UI that is displayed to user

    // Method to load the current screen
    public void displayCurrentScreen() {
        currentScreen.displayScreen();
    }

    // Method to load the set the currentScreen (e.g. start game, options)
    public void setCurrentScreen(Screen s) {
        currentScreen = s;
    }

    public Screen getCurrentScreen() {
        return currentScreen;
    }

    public static void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
