package javatro.display.screens;

import static org.junit.jupiter.api.Assertions.*;

import javatro.core.JavatroCore;
import javatro.core.JavatroException;
import javatro.display.UI;
import javatro.manager.JavatroManager;
import javatro.manager.options.Option;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class StartScreenTest {

    private StartScreen startScreen;

    @BeforeEach
    public void setUp() throws JavatroException {
        startScreen = new StartScreen();
        UI javatroView = new UI();

        JavatroCore javatroCore = new JavatroCore();

        /**
         * The manager responsible for handling interactions between the view and core components.
         */
        JavatroManager javatroManager;

        try {
            javatroManager = new JavatroManager(javatroView, javatroCore);
        } catch (JavatroException e) {
            throw new RuntimeException(e);
        }

        JavatroManager.runningTests = true;
    }

    @Test
    public void testInitialization() {
        assertNotNull(startScreen);
    }

    @Test
    public void testCommandMapPopulation() {
        assertEquals(3, startScreen.getCommandMap().size());

        Option startGameOption = startScreen.getCommandMap().get(0);
        Option helpMenuOption = startScreen.getCommandMap().get(1);
        Option exitGameOption = startScreen.getCommandMap().get(2);

        assertNotNull(startGameOption);
        assertNotNull(helpMenuOption);
        assertNotNull(exitGameOption);
    }

    @Test
    public void testDisplayScreen() {
        assertDoesNotThrow(() -> startScreen.displayScreen());
    }

    @Test
    public void testInvalidCommandHandling() {
        assertThrows(IndexOutOfBoundsException.class, () -> startScreen.getCommandMap().get(100));
    }

    @Test
    public void testStressTestStartScreen() {
        for (int i = 0; i < 500; i++) {
            assertDoesNotThrow(() -> startScreen.displayScreen());
        }
    }

    @Test
    public void testDisplayOptions() {
        // Capture printed output
        UI.printBorderedContent("WELCOME TO JAVATRO", new ArrayList<>());

        List<Option> commandMap = startScreen.getCommandMap();
        assertEquals(3, commandMap.size());

        // Verify that each command description is correctly displayed
        for (Option option : commandMap) {
            assertNotNull(option.getDescription());
            assertFalse(option.getDescription().isEmpty());
        }
    }

    @Test
    public void testDisplayOptionsOutput() {
        List<Option> commandMap = startScreen.getCommandMap();
        assertEquals(3, commandMap.size());

        // Capture console output
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        startScreen.displayOptions();

        System.setOut(originalOut); // Restore original System.out

        String output = outputStream.toString();
        assertTrue(output.contains("[1]")); // Checking if options are listed correctly
        assertTrue(output.contains("[2]"));
        assertTrue(output.contains("[3]"));
    }

    @Test
    public void testExitGameCommandExecution() {
        Option exitGameOption = startScreen.getCommandMap().get(2);
        try {
            exitGameOption.execute();
        } catch (JavatroException e) {
            throw new RuntimeException(e);
        }
        assertTrue(JavatroManager.isExitTriggered); // Confirming the exit was triggered
    }

    @Test
    public void testHelpMenuCommandExecution() {
        Option helpMenuOption = startScreen.getCommandMap().get(1);
        try {
            helpMenuOption.execute();
        } catch (JavatroException e) {
            throw new RuntimeException(e);
        }
        assertInstanceOf(HelpScreen.class, UI.getCurrentScreen());
    }

    @Test
    public void testStartGameCommandExecution() {
        Option startGameOption = startScreen.getCommandMap().get(0);
        try {
            startGameOption.execute();
        } catch (JavatroException e) {
            throw new RuntimeException(e);
        }
        assertInstanceOf(GameScreen.class, UI.getCurrentScreen());
    }

    @Test
    public void testLogoInitialization() {
        // Verify that the logo is initialized and not null
        assertNotNull(StartScreen.JAVATRO_LOGO, "Logo should be initialized");

        // Verify that the logo content is not empty
        assertFalse(StartScreen.JAVATRO_LOGO.isEmpty(), "Logo content should not be empty");
    }

    @Test
    public void testDisplayLogoOutput() {
        // Capture console output
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Call the displayScreen() method which should print the logo
        startScreen.displayScreen();

        // Restore the original System.out
        System.setOut(originalOut);

        // Get the printed output
        String output = outputStream.toString();

        // Verify the logo is printed
        assertTrue(
                output.contains("javatro Logo") || !output.trim().isEmpty(),
                "The logo should be printed to the console");
    }

    @Test
    public void testFileNotFound() {
        try {
            StartScreen.initializeLogo("/invalid_path/javatro_logo.txt");
            assertEquals("javatro Logo", StartScreen.getLogo()); // Should fall back to default logo
        } catch (JavatroException e) {
            assertTrue(e.getMessage().contains("Error loading logo"));
        }
    }
}
