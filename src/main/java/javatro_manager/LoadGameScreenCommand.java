package javatro_manager;


public class LoadGameScreenCommand implements Command {

    public LoadGameScreenCommand() {}

    @Override
    public void execute() {
        // Update the main screen to show game screen
        JavatroManager.setScreen(JavatroManager.getGameScreen());
    }

    @Override
    public String getDescription() {
        return "Start Game";
    }
}
