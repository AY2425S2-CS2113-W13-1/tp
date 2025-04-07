package display.screens;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import javatro.core.JavatroCore;
import javatro.core.JavatroException;
import javatro.display.UI;
import javatro.display.screens.Screen;
import javatro.manager.JavatroManager;
import javatro.manager.options.Option;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public abstract class ScreenTest {

    protected final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    protected final PrintStream originalOut = System.out;

    protected List<Class<?>> expectedCommands = new ArrayList<>();

    protected JavatroManager javatroManager;

    @BeforeEach
    public void setUp() {
        UI javatroView = new UI();

        JavatroCore javatroCore = new JavatroCore();

        try {
            javatroManager = new JavatroManager(javatroView, javatroCore);
        } catch (JavatroException e) {
            throw new RuntimeException(e);
        }

        JavatroManager.runningTests = true;

        System.setOut(new PrintStream(outContent));
    }

    protected void compareCommandListTypes(List<Class<?>> expectedTypes, List<Option> actualList) {
        if (expectedTypes.size() != actualList.size()) {
            fail(
                    "Mismatch in number of commands. Expected: "
                            + expectedTypes.size()
                            + ", but got: "
                            + actualList.size());
        }

        for (int i = 0; i < expectedTypes.size(); i++) {
            Class<?> expectedType = expectedTypes.get(i);
            Option actualOption = actualList.get(i);

            if (!expectedType.isInstance(actualOption)) {
                fail(
                        "Mismatch at index "
                                + i
                                + ". Expected type: "
                                + expectedType.getSimpleName()
                                + ", but got: "
                                + actualOption.getClass().getSimpleName());
            }
        }
    }

    protected void compareScreenType(Object screen, Class<?> expectedType) {
        if (!expectedType.isInstance(screen)) {
            fail(
                    "Expected screen type: "
                            + expectedType.getSimpleName()
                            + ", but got: "
                            + screen.getClass().getSimpleName());
        }
    }

    @Test
    public void testScreenType() {
        // Get the current screen
        Screen currentScreen = UI.getCurrentScreen();

        // Use the inherited compareScreenType method to check type
        compareScreenType(currentScreen, getExpectedScreenType());
    }

    // Abstract method to be implemented by child classes
    protected abstract Class<?> getExpectedScreenType();

    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
        outContent.reset();
    }

    protected String getOutput() {
        return outContent.toString().trim();
    }

    protected String readExpectedOutput(String fileName) throws IOException {
        String path = "src/test/resources/screens/" + fileName;
        return Files.readString(Paths.get(path)).trim();
    }

    protected void compareOutputToFile(String fileName) throws IOException {
        String actualOutput = getOutput();
        //    CSVUtils.writeSampleToCSV("src/test/resources/screens/nigga.txt",actualOutput);
        String expectedOutput = readExpectedOutput(fileName);

        if (!actualOutput.equals(expectedOutput)) {
            fail("Output mismatch for file: " + fileName);
        }
    }

    @Test
    public void testAccessingClearedCommandMap() {
        Screen currentScreen = UI.getCurrentScreen();
        currentScreen.clearCommandMap(); // Clear all commands

        Exception exception =
                assertThrows(
                        IndexOutOfBoundsException.class,
                        () -> {
                            currentScreen
                                    .getCommandMap()
                                    .get(0)
                                    .execute(); // Attempting to access the first command in an
                                                // empty list
                        });

        assertTrue(exception.getMessage().contains("Index 0 out of bounds"));
    }

    //  @Test
    //  public void testNavigateThroughAllOptions() {
    //    Screen currentScreen = UI.getCurrentScreen();
    //    List<Option> options = currentScreen.getCommandMap();
    //
    //    if (options.isEmpty()) {
    //      fail("No options available to navigate.");
    //    }
    //
    //    for (int i = 0; i < options.size(); i++) {
    //      Option option = options.get(i);
    //      String optionName = option.getClass().getSimpleName();
    //      try {
    //        // Execute the option and capture output
    //        boolean success = option.execute();
    //
    //        // Check if execution was successful
    //        assertTrue(success, "Option execution failed for: " + optionName);
    //
    //        // Verify output for each option using a standardized file naming format
    //        String outputFileName = getExpectedScreenType().getSimpleName() + "_" + optionName +
    // "_Output.txt";
    //
    //        try {
    //          compareOutputToFile(outputFileName);
    //        } catch (IOException e) {
    //          fail("Could not compare output to file: " + outputFileName);
    //        }
    //
    //        // Reset screen to original state before testing next option
    //        resetToInitialScreen();
    //      } catch (Exception e) {
    //        fail("Failed to execute option: " + optionName + " - " + e.getMessage());
    //      }
    //    }
    //  }
    //
    //  /**
    //   * Resets the screen to its initial state. Must be overridden by child classes if needed.
    //   */
    //  protected void resetToInitialScreen() throws JavatroException {
    //    UI.setCurrentScreen(newInstanceOfScreen());
    //  }
    //
    //  /**
    //   * Creates a new instance of the expected screen type. Must be overridden by child classes.
    //   */
    //  protected abstract Screen newInstanceOfScreen() throws JavatroException;

}
