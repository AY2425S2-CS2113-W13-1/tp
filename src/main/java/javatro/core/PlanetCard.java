// @@author Markneoneo
package javatro.core;

import java.util.EnumMap;
import java.util.Map;

/**
 * Represents a planet card associated with a specific poker hand type. Each card provides chip and
 * multiplier increments and can be leveled up to increase these bonuses. Maintains static level
 * tracking for all hand types.
 *
 * <p>Predefined cards are initialized statically and accessed through factory methods. Level
 * management affects all instances of a particular hand type.
 */
public class PlanetCard {
    /**
     * Maps each poker hand type to its current enhancement level. Initialized to level 1 for all
     * hand types.
     */
    public static final Map<PokerHand.HandType, Integer> LEVELS =
            new EnumMap<>(PokerHand.HandType.class);

    /**
     * Registry of all predefined planet cards mapped to their corresponding hand types. Populated
     * during class initialization.
     */
    private static final Map<PokerHand.HandType, PlanetCard> CARDS =
            new EnumMap<>(PokerHand.HandType.class);

    static {
        // Initialize base levels for all known hand types
        for (PokerHand.HandType handType : PokerHand.HandType.values()) {
            LEVELS.put(handType, 1);
        }

        // Populate predefined planet cards with their configurations
        CARDS.put(
                PokerHand.HandType.HIGH_CARD,
                new PlanetCard("Pluto", 10, 1, PokerHand.HandType.HIGH_CARD, "planet_pluto.txt"));
        CARDS.put(
                PokerHand.HandType.PAIR,
                new PlanetCard("Mercury", 15, 1, PokerHand.HandType.PAIR, "planet_mercury.txt"));
        CARDS.put(
                PokerHand.HandType.TWO_PAIR,
                new PlanetCard("Uranus", 20, 1, PokerHand.HandType.TWO_PAIR, "planet_uranus.txt"));
        CARDS.put(
                PokerHand.HandType.THREE_OF_A_KIND,
                new PlanetCard(
                        "Venus", 20, 2, PokerHand.HandType.THREE_OF_A_KIND, "planet_venus.txt"));
        CARDS.put(
                PokerHand.HandType.STRAIGHT,
                new PlanetCard("Saturn", 30, 3, PokerHand.HandType.STRAIGHT, "planet_saturn.txt"));
        CARDS.put(
                PokerHand.HandType.FLUSH,
                new PlanetCard("Jupiter", 15, 2, PokerHand.HandType.FLUSH, "planet_jupiter.txt"));
        CARDS.put(
                PokerHand.HandType.FULL_HOUSE,
                new PlanetCard("Earth", 25, 2, PokerHand.HandType.FULL_HOUSE, "planet_earth.txt"));
        CARDS.put(
                PokerHand.HandType.FOUR_OF_A_KIND,
                new PlanetCard(
                        "Mars", 30, 3, PokerHand.HandType.FOUR_OF_A_KIND, "planet_mars.txt"));
        CARDS.put(
                PokerHand.HandType.STRAIGHT_FLUSH,
                new PlanetCard(
                        "Neptune", 40, 4, PokerHand.HandType.STRAIGHT_FLUSH, "planet_neptune.txt"));
        CARDS.put(
                PokerHand.HandType.ROYAL_FLUSH,
                new PlanetCard(
                        "Neptune", 40, 4, PokerHand.HandType.ROYAL_FLUSH, "planet_neptune.txt"));
        CARDS.put(
                PokerHand.HandType.FIVE_OF_A_KIND,
                new PlanetCard(
                        "Planet X",
                        35,
                        3,
                        PokerHand.HandType.FIVE_OF_A_KIND,
                        "planet_planetX.txt"));
        CARDS.put(
                PokerHand.HandType.FLUSH_HOUSE,
                new PlanetCard("Ceres", 40, 4, PokerHand.HandType.FLUSH_HOUSE, "planet_ceres.txt"));
        CARDS.put(
                PokerHand.HandType.FLUSH_FIVE,
                new PlanetCard("Eris", 50, 3, PokerHand.HandType.FLUSH_FIVE, "planet_eris.txt"));
    }

    private final String name;
    private final int chipIncrement;
    private final int multiIncrement;
    private final PokerHand.HandType handType;
    private final String path;

    /**
     * Constructs a planet card with specified attributes. Private to enforce singleton instances
     * through predefined cards.
     *
     * @param name Display name of the planet
     * @param chipIncrement Base value added to chip count per level
     * @param multiIncrement Base value added to multiplier per level
     * @param handType Associated poker hand type
     * @param path Resource path for planet visualization
     */
    private PlanetCard(
            String name,
            int chipIncrement,
            int multiIncrement,
            PokerHand.HandType handType,
            String path) {
        this.name = name;
        this.chipIncrement = chipIncrement;
        this.multiIncrement = multiIncrement;
        this.handType = handType;
        this.path = path;
    }

    // Region: Accessors --------------------------------------------------------

    /**
     * Retrieves the predefined planet card for a specific hand type.
     *
     * @param handType Poker hand type to look up
     * @return Associated planet card instance
     * @throws AssertionError if no card exists for the specified hand type
     */
    public static PlanetCard getForHand(PokerHand.HandType handType) {
        PlanetCard card = CARDS.get(handType);
        assert card != null : "Missing planet card configuration for: " + handType;
        return card;
    }

    /**
     * Gets current enhancement level for a hand type.
     *
     * @param handType Poker hand type to check
     * @return Current level (minimum 1)
     */
    public static int getLevel(PokerHand.HandType handType) {
        return LEVELS.get(handType);
    }

    /**
     * @return Base chip increment without level scaling
     */
    public int getChipIncrement() {
        return chipIncrement;
    }

    /**
     * Calculates total chip increment for a hand type at its current level.
     *
     * @param handType Poker hand type to check
     * @return Total chip value including level scaling
     */
    public static int getChipIncrement(PokerHand.HandType handType) {
        PlanetCard card = CARDS.get(handType);
        assert card != null : "Missing chip configuration for: " + handType;
        return card != null ? card.chipIncrement : 0;
    }

    /**
     * @return Base multiplier increment without level scaling
     */
    public int getMultiIncrement() {
        return multiIncrement;
    }

    /**
     * Calculates total multiplier increment for a hand type at its current level.
     *
     * @param handType Poker hand type to check
     * @return Total multiplier value including level scaling
     */
    public static int getMultiIncrement(PokerHand.HandType handType) {
        PlanetCard card = CARDS.get(handType);
        assert card != null : "Missing multiplier configuration for: " + handType;
        return card != null ? card.multiIncrement : 0;
    }

    /**
     * Enhances this card's associated hand type by one level. Affects all instances using this hand
     * type.
     */
    public void apply() {
        LEVELS.put(handType, LEVELS.get(handType) + 1);
    }

    /**
     * @return Planetary display name
     */
    public String getName() {
        return name;
    }

    /**
     * @return Associated poker hand type
     */
    public PokerHand.HandType getHandType() {
        return handType;
    }

    /**
     * @return Resource path for planetary visualization
     */
    public String getPath() {
        return path;
    }
}
