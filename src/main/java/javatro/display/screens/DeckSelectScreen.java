// @@author Markneoneo
package javatro.display.screens;

import javatro.core.Deck;
import javatro.core.JavatroException;
import javatro.manager.options.DeckSelectOption;

/**
 * Screen for selecting player deck configuration at game start.
 *
 * <p>Provides different deck types with unique gameplay modifiers:
 *
 * <ul>
 *   <li>Red Deck: Enhanced discard capabilities
 *   <li>Blue Deck: Additional hand opportunities
 *   <li>Checkered Deck: Balanced suit distribution
 *   <li>Abandoned Deck: Simplified card composition
 * </ul>
 */
public class DeckSelectScreen extends Screen {

    /**
     * Constructs deck selection screen with available deck options.
     *
     * @throws JavatroException if screen initialization fails
     */
    public DeckSelectScreen() throws JavatroException {
        super("Select Your Deck");

        // Initialize deck options with descriptions and effects
        commandMap.add(new DeckSelectOption("Red Deck: +1 Discards per Round", Deck.DeckType.RED));
        commandMap.add(new DeckSelectOption("Blue Deck: +1 Hands per Round", Deck.DeckType.BLUE));
        commandMap.add(
                new DeckSelectOption(
                        "Checkered Deck: Start with 26 Hearts, 26 Spades",
                        Deck.DeckType.CHECKERED));
        commandMap.add(
                new DeckSelectOption(
                        "Abandoned Deck: Start with no Face Cards (K, Q, J)",
                        Deck.DeckType.ABANDONED));

        assert commandMap.size() == 4 : "Should have exactly 4 deck options";
    }

    /** Displays deck selection interface (handled by superclass options rendering). */
    @Override
    public void displayScreen() {
        // Intentional no-op - screen content handled by option display
    }
}
