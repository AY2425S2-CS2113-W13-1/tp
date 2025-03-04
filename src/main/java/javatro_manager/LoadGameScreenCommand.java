package javatro_manager;

import javatro_view.GameScreen;
import javatro_view.JavatroView;

public class LoadGameScreenCommand implements Command {

    private GameScreen gameScreen;

    public LoadGameScreenCommand(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    @Override
    public void execute() {
        JavatroManager.setScreen(gameScreen);
        gameScreen.displayScreen();
        JavatroManager.getInput();
        JavatroView.clearConsole();
    }
}
