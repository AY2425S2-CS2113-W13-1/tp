package javatro_manager;

import Javatro.JavatroException;

import javatro_view.JavatroView;

public class LoadGameScreenCommand implements Command {

    public LoadGameScreenCommand() {}

    @Override
    public void execute() throws JavatroException {

        JavatroManager.beginGame();

        // Update the main screen to show game screen
        JavatroManager.setScreen(JavatroView.getGameScreen());
    }

    @Override
    public String getDescription() {
        return "Start Game";
    }
}
