package display.screens;

import static org.junit.jupiter.api.Assertions.fail;

import javatro.core.JavatroException;
import javatro.display.UI;
import javatro.manager.options.Option;
import javatro.manager.options.StartGameOption;
import javatro.utilities.csvutils.CSVUtils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

public class LoadRunScreenWithEmptySaveFileTest extends ScreenTest {

    @BeforeEach
    public void setUp() {
        try {
            CSVUtils.clearCSVFile(SAVEFILE_PATH);
        } catch (IOException e) {
            System.out.println("Could not update savefile");
        }

        super.setUp();

        try {
            UI.getStartScreen().getCommand(0).execute();
        } catch (JavatroException e) {
            fail("Failed to Set Screen: " + e.getMessage());
        }
    }

    @Test
    public void testCommandMatchWithSave() {
        List<Class<?>> expectedCommands = List.of(StartGameOption.class);

        List<Option> actualCommands = UI.getCurrentScreen().getCommandMap();
        compareCommandListTypes(expectedCommands, actualCommands);
    }

    @Test
    public void testRunSelectScreenOutputWithSave() throws IOException {
        pipeOutputToFile("data.txt", UI.getRunSelectScreen());

        compareOutputToFile2("RunSelectScreen_EmptySave.txt");
    }
}
