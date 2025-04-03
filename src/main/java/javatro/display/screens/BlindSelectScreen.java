package javatro.display.screens;

import javatro.core.Ante;
import javatro.core.JavatroCore;
import javatro.core.JavatroException;
import javatro.display.UI;
import javatro.manager.options.AcceptBlindOption;
import javatro.manager.options.RejectBlindOption;

import java.util.ArrayList;
import java.util.List;

// @@author swethacool

/**
 * Represents the screen for selecting the blind in the game. This screen provides options to accept
 * or reject the blind and visually displays the current blind settings.
 */
public class BlindSelectScreen extends Screen {

    /**
     * Constructs a BlindSelectScreen and initializes the available options.
     *
     * @throws JavatroException if there is an error initializing the screen.
     */
    public BlindSelectScreen() throws JavatroException {
        super("BLIND MENU");

        commandMap.add(new AcceptBlindOption());
        commandMap.add(new RejectBlindOption());
    }

    /**
     * Displays the Blind Selection screen with highlighted sections based on the current blind
     * setting.
     */
    @Override
    public void displayScreen() {
        int activeSelection = getCurrentBlindIndex();
        int sectionWidth = UI.BORDER_WIDTH / 3;

        drawTopBorder(activeSelection, sectionWidth);
        drawContentRows(activeSelection, sectionWidth);
        drawBottomBorder(activeSelection, sectionWidth);
    }

    /**
     * Determines the index of the current active blind setting.
     *
     * @return the index of the current blind (0 for Small Blind, 1 for Large Blind, 2 for Boss
     *     Blind).
     */
    private int getCurrentBlindIndex() {
        Ante.Blind currentBlind = JavatroCore.getAnte().getBlind();
        return (currentBlind == Ante.Blind.SMALL_BLIND)
                ? 0
                : (currentBlind == Ante.Blind.LARGE_BLIND) ? 1 : 2;
    }

    /**
     * Draws the top border of the screen with highlighted sections.
     *
     * @param activeSelection the index of the currently active blind.
     * @param sectionWidth the width of each section.
     */
    private void drawTopBorder(int activeSelection, int sectionWidth) {
        System.out.print(getHighlightedChar(activeSelection == 0, UI.TOP_LEFT));

        for (int i = 0; i < 3; i++) {
            if (i == 1) {
                System.out.print(getHighlightedChar(activeSelection <= 1, UI.T_DOWN));
            }

            String horizontalLine = String.valueOf(UI.HORIZONTAL).repeat(sectionWidth - 1);
            System.out.print(getHighlightedChar(activeSelection == i, horizontalLine));

            if (i == 1) {
                System.out.print(getHighlightedChar(activeSelection >= 1, UI.T_DOWN));
            }
        }

        System.out.println(getHighlightedChar(activeSelection == 2, UI.TOP_RIGHT));
    }

    /**
     * Draws the content rows of the screen, displaying blind options and their corresponding
     * values.
     *
     * @param activeSelection the index of the currently active blind.
     * @param sectionWidth the width of each section.
     */
    private void drawContentRows(int activeSelection, int sectionWidth) {
        List<String[]> contentRows = generateContentData();

        for (String[] row : contentRows) {
            System.out.print(getHighlightedChar(activeSelection == 0, UI.VERTICAL));

            for (int i = 0; i < 3; i++) {
                if (i == 1) {
                    System.out.print(getHighlightedChar(activeSelection <= 1, UI.VERTICAL));
                }

                String centeredText = centerInSection(row[i], sectionWidth - 1);
                System.out.print(getHighlightedChar(activeSelection == i, centeredText));

                if (i == 1) {
                    System.out.print(getHighlightedChar(activeSelection >= 1, UI.VERTICAL));
                }
            }

            System.out.println(getHighlightedChar(activeSelection == 2, UI.VERTICAL));
        }
    }

    /**
     * Generates the content data for the blind selection display.
     *
     * @return a list of string arrays representing different rows of the screen.
     */
    private List<String[]> generateContentData() {
        List<String[]> contentRows = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            contentRows.add(new String[] {"", "", ""});
        }

        contentRows.add(
                new String[] {
                    Ante.Blind.SMALL_BLIND.getName(),
                    Ante.Blind.LARGE_BLIND.getName(),
                    Ante.Blind.BOSS_BLIND.getName()
                });

        contentRows.add(new String[] {"", "", ""});

        contentRows.add(
                new String[] {
                    String.valueOf(
                            (int)
                                    (JavatroCore.getAnte().getAnteScore()
                                            * Ante.Blind.SMALL_BLIND.getMultiplier())),
                    String.valueOf(
                            (int)
                                    (JavatroCore.getAnte().getAnteScore()
                                            * Ante.Blind.LARGE_BLIND.getMultiplier())),
                    String.valueOf(
                            (int)
                                    (JavatroCore.getAnte().getAnteScore()
                                            * Ante.Blind.BOSS_BLIND.getMultiplier()))
                });

        for (int i = 0; i < 6; i++) {
            contentRows.add(new String[] {"", "", ""});
        }

        return contentRows;
    }

    /**
     * Draws the bottom border of the screen with highlighted sections.
     *
     * @param activeSelection the index of the currently active blind.
     * @param sectionWidth the width of each section.
     */
    private void drawBottomBorder(int activeSelection, int sectionWidth) {
        System.out.print(getHighlightedChar(activeSelection == 0, UI.BOTTOM_LEFT));

        for (int i = 0; i < 3; i++) {
            if (i == 1) {
                System.out.print(getHighlightedChar(activeSelection <= 1, UI.T_UP));
            }

            String horizontalLine = String.valueOf(UI.HORIZONTAL).repeat(sectionWidth - 1);
            System.out.print(getHighlightedChar(activeSelection == i, horizontalLine));

            if (i == 1) {
                System.out.print(getHighlightedChar(activeSelection >= 1, UI.T_UP));
            }
        }

        System.out.println(getHighlightedChar(activeSelection == 2, UI.BOTTOM_RIGHT));
    }

    /**
     * Centers text within a given width.
     *
     * @param text the text to center.
     * @param width the width of the section.
     * @return the centered text.
     */
    private String centerInSection(String text, int width) {
        int textLength = text.length();
        int padding = Math.max(0, width - textLength);
        int leftPad = padding / 2;
        int rightPad = padding - leftPad;

        return " ".repeat(leftPad) + text + " ".repeat(rightPad);
    }

    /**
     * Highlights a character if it is part of the active selection.
     *
     * @param isActive whether the section is active.
     * @param character the character to highlight.
     * @return the highlighted character.
     */
    private String getHighlightedChar(boolean isActive, char character) {
        return getHighlightedChar(isActive, String.valueOf(character));
    }

    /**
     * Highlights a string if it is part of the active selection.
     *
     * @param isActive whether the section is active.
     * @param text the text to highlight.
     * @return the highlighted text.
     */
    private String getHighlightedChar(boolean isActive, String text) {
        if (isActive) {
            return UI.YELLOW + text + UI.END;
        } else {
            return text;
        }
    }
}
