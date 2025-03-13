package javatro_manager;


public class LoadOptionsScreenCommand implements Command {

    public LoadOptionsScreenCommand() {}

    @Override
    public void execute() {
        JavatroManager.setScreen(JavatroManager.getOptionScreen());
    }

    @Override
    public String getDescription() {
        return "Select Options";
    }
}
