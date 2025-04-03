// @@author Markneoneo
/**
 * Provides the user interface layer for the Javatro application, handling all presentation logic
 * and user interaction components.
 *
 * <p>This package contains all classes responsible for:
 *
 * <ul>
 *   <li>Managing screen transitions and UI state
 *   <li>Rendering game elements using ANSI art and colored text
 *   <li>Handling user input and menu navigation
 *   <li>Displaying game state information and statistics
 *   <li>Implementing complex visual layouts and formatting
 * </ul>
 *
 * <h2>Key Components</h2>
 *
 * <h3>Core UI Management</h3>
 *
 * {@link javatro.display.UI} - Singleton controller managing screen stack, input handling, and
 * observer notifications. Implements the main display loop and coordinates all visual components.
 *
 * <h3>Screen Implementations</h3>
 *
 * {@link javatro.display.screens.Screen} - Abstract base class for all display screens. Concrete
 * implementations include:
 *
 * <ul>
 *   <li>{@link javatro.display.screens.GameScreen} - Main gameplay interface showing scores, hands,
 *       and jokers
 *   <li>{@link javatro.display.screens.DeckViewScreen} - Detailed deck composition table view
 *   <li>{@link javatro.display.screens.HelpScreen} - Help menu with game rules and instructions
 *   <li>{@link javatro.display.screens.BlindSelectScreen} - Blind selection interface with
 *       risk/reward visualization
 *   <li>{@link javatro.display.screens.StartScreen} - Initial menu with game startup options
 *   <li>{@link javatro.display.screens.WinGameScreen}/{@link javatro.display.screens.LoseScreen} -
 *       Endgame state displays
 * </ul>
 *
 * <h3>Rendering Utilities</h3>
 *
 * Contains specialized components for visual presentation:
 *
 * <ul>
 *   <li>{@link javatro.display.CardRenderer} - Renders individual cards as ASCII art
 *   <li>{@link javatro.display.UI} formatting methods - Handle complex text layout and borders
 *   <li>ANSI art loading and display functionality
 * </ul>
 *
 * <h2>Design Patterns</h2>
 *
 * <ul>
 *   <li>Singleton pattern ({@link javatro.display.UI}) for centralized display control
 *   <li>Observer pattern for screen state changes
 *   <li>Template method pattern in {@link javatro.display.screens.Screen} hierarchy
 *   <li>Factory methods for exception creation
 * </ul>
 *
 * <h2>Exception Handling</h2>
 *
 * Uses {@link javatro.core.JavatroException} for all display-layer errors with colored error
 * messages.
 *
 * @see javatro.core.JavatroException
 * @see javatro.manager.options
 */
package javatro.display;
