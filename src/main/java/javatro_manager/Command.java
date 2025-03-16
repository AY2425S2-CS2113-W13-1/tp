package javatro_manager;

import Javatro.JavatroException;

public interface Command {
    default String getDescription() {
        return "";
    }

    public void execute() throws JavatroException;
}
