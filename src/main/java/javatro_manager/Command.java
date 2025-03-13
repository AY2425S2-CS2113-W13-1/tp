package javatro_manager;

public interface Command {
    default String getDescription() {
        return "";
    }

    public void execute();
}
