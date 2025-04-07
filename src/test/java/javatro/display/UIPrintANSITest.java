package javatro.display;

import javatro.display.UI;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class UIPrintANSITest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    public void testPrintANSI_ValidFile() {
        // Test with a valid file (ensure the file exists under src/test/resources/ansi)
        assertDoesNotThrow(() -> UI.printANSI("javatro_logo.txt"));

        String output = outContent.toString().trim();

        assertFalse(output.contains("ANSI TEXT"), "Should not display fallback message for valid file.");
        assertFalse(output.isEmpty(), "Output should not be empty when valid file is loaded.");
    }

    @Test
    public void testPrintANSI_InvalidFile() {
        // Test with a non-existent file
        UI.printANSI("nonexistent.txt");

        String output = outContent.toString().trim();
        String errorOutput = errContent.toString().trim();

        assertTrue(output.contains("ANSI TEXT"), "Fallback message should be displayed for invalid file.");
        assertTrue(errorOutput.contains("Error loading ANSI art"), "Error message should be printed to stderr.");
    }

    @Test
    public void testPrintANSI_NullFileName() {
        // Test with a null file name
        assertThrows(AssertionError.class, () -> UI.printANSI(null), "Null file name should trigger assertion.");
    }

    @Test
    public void testPrintANSI_EmptyFileName() {
        // Test with an empty file name
        assertThrows(AssertionError.class, () -> UI.printANSI(""), "Empty file name should trigger assertion.");
    }
}
