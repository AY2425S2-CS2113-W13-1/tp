package javatro.manager.options;

import javatro.core.JavatroException;
import javatro.display.UI;
import javatro.manager.JavatroManager;

public class DeckOption implements Option {

    @Override
    public String getDescription() {
        return "View Deck";
    }

    @Override
    public void execute() throws JavatroException {
        JavatroManager.setScreen(UI.getDeckScreen());
    }
}
