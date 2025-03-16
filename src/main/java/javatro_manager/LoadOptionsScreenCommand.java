package javatro_manager;

import javatro_view.JavatroView;

public class LoadOptionsScreenCommand implements Command {

    public LoadOptionsScreenCommand() {}

    @Override
    public void execute() {
        JavatroManager.setScreen(JavatroView.getOptionScreen());
    }

    @Override
    public String getDescription() {
        return "Select Options";
    }
}
