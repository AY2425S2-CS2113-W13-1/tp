package javatro_manager;


public class LoadStartScreenCommand implements Command {

    public LoadStartScreenCommand() {}

    @Override
    public void execute() {
        JavatroManager.setScreen(JavatroManager.getStartScreen());
    }

    @Override
    public String getDescription() {
        return "Main Menu";
    }
}
