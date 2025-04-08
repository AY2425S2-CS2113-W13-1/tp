package javatro.display.screens;

import static org.junit.jupiter.api.Assertions.fail;

import javatro.core.JavatroCore;
import javatro.core.JavatroException;
import javatro.display.UI;
import javatro.manager.JavatroManager;
import javatro.manager.options.Option;
import javatro.utilities.csvutils.CSVUtils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public abstract class ScreenTest {

    protected static final String SAMPLE_DATA =
            "0,1,0,4,5,1,SMALL"
                    + " BLIND,0,0,RED,3H,5H,8H,10H,4H,JD,JH,10D,"
                    + "-,-,-,-,-,1,1,1,1,1,1,1,1,1,1,1,1,1,9D,10C,AC,6H,AS,2C,"
                    + "3C,AD,6C,JC,5D,6S,3S,7C,5C,QH,QS,10S,KD,7H,QC,7D,8S,QD,"
                    + "KS,2D,3D,9H,2H,JS,2S,4C,8C,9C,AH,4D,4S,KH,9S,6D,8D,"
                    + "7S,5S,KC,1cdb399415e1c91d8ef8b7bd443598dd014c2dc996e65e8b6c93f703f4001437";
    protected final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    protected final PrintStream originalOut = System.out;
    protected final String SAVEFILE_PATH = "./savefile.csv";

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
        CSVUtils.writeSampleToCSV("src/test/resources/screens/data.txt", actualOutput);
        String expectedOutput = readExpectedOutput(fileName);

        if (!actualOutput.equals(expectedOutput)) {
            fail("Output mismatch for file: " + fileName);
        }
    }

    protected void compareOutputToFile2(String expectedFileName) throws IOException {
        // Read the expected output file
        String expectedOutput = normalizeLineEndings(readExpectedOutput(expectedFileName));

        // Read the actual output file (data.txt)
        String actualOutput = normalizeLineEndings(readExpectedOutput("data.txt"));

        // Compare outputs
        if (!actualOutput.equals(expectedOutput)) {
            fail("Output mismatch between data.txt and " + expectedFileName);
        }
    }

    private String normalizeLineEndings(String input) {
        return input.replace("\r\n", "\n").replace("\r", "\n").stripTrailing();
    }

    /**
     * Pipes the output of the current screen to a file during testing.
     *
     * @param fileName The name of the file to compare against.
     * @param renderable The renderable screen object.
     * @throws IOException If an I/O error occurs.
     */
    protected void pipeOutputToFile(String fileName, Screen renderable) throws IOException {
        // Prepare to capture the output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            JavatroManager.setScreen(renderable);
        } catch (JavatroException e) {
            throw new RuntimeException(e);
        }

        // Save the captured output to a file
        try (FileOutputStream fos =
                new FileOutputStream("src/test/resources/screens/" + fileName)) {
            fos.write(outContent.toString().getBytes());
        } finally {
            // Reset the standard output
            System.setOut(originalOut);
        }
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
