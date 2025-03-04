package javatro_manager;

import javatro_view.JavatroView;
import javatro_view.StartScreen;

public class LoadStartScreenCommand implements Command {
    private StartScreen startScreen;

    public LoadStartScreenCommand(StartScreen startScreen) {
        this.startScreen = startScreen;
    }

    @Override
    public void execute() {
        JavatroManager.setScreen(startScreen);
        startScreen.displayScreen();
        JavatroManager.getInput();
        JavatroView.clearConsole();
    }
}
