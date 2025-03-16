package javatro_manager;

import javatro_view.JavatroView;

public class LoadStartScreenCommand implements Command {

    public LoadStartScreenCommand() {}

    @Override
    public void execute() {
        JavatroManager.setScreen(JavatroView.getStartScreen());
    }

    @Override
    public String getDescription() {
        return "Main Menu";
    }
}
