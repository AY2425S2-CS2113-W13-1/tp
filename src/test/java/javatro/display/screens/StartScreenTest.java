package javatro.display.screens;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import javatro.core.JavatroException;

import javatro.manager.options.ExitGameOption;
import javatro.manager.options.HelpMenuOption;
import javatro.manager.options.Option;
import javatro.manager.options.RunSelectOption;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class StartScreenTest extends ScreenTest {

    private StartScreen startScreen;

    @BeforeEach
    public void setUp() {
        super.setUp();
        try {
            startScreen = new StartScreen();
        } catch (JavatroException e) {
            fail("Failed to create start screen" + e);
        }
    }

    @Test
    public void commandMatchCheck() {
        expectedCommands.add(RunSelectOption.class);
        expectedCommands.add(HelpMenuOption.class);
        expectedCommands.add(ExitGameOption.class);

        List<Option> actualCommands = startScreen.getCommandMap();

        compareCommandListTypes(expectedCommands, actualCommands);
    }

    @Test
    public void commandCountCheck() {
        List<Option> actualCommands = startScreen.getCommandMap();

        assertEquals(3, actualCommands.size());
    }
}
