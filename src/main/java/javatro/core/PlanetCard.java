package javatro.core;

import java.util.EnumMap;
import java.util.Map;

public class PlanetCard {
    private static final Map<PokerHand.HandType, Integer> LEVELS =
            new EnumMap<>(PokerHand.HandType.class);
    private static final Map<PokerHand.HandType, PlanetCard> CARDS =
            new EnumMap<>(PokerHand.HandType.class);

    static {
        // Initialize all levels to 1
        for (PokerHand.HandType handType : PokerHand.HandType.values()) {
            LEVELS.put(handType, 1);
        }

        // Predefine all planet cards with unique paths
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
                PokerHand.HandType.FIVE_OF_A_KIND,
                new PlanetCard(
                        "Planet X",
                        35,
                        3,
                        PokerHand.HandType.FIVE_OF_A_KIND,
                        "planet_planet_x.txt"));
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

    public String getPath() {
        return path;
    }

    // Get predefined PlanetCard for a hand type
    public static PlanetCard getForHand(PokerHand.HandType handType) {
        return CARDS.get(handType);
    }

    // Static method to get current level for a hand type
    public static int getLevel(PokerHand.HandType handType) {
        return LEVELS.getOrDefault(handType, 1);
    }

    // Static method to get chip increment for a hand type
    public static int getChipIncrement(PokerHand.HandType handType) {
        PlanetCard card = CARDS.get(handType);
        return card != null ? card.chipIncrement : 0;
    }

    // Static method to get multi increment for a hand type
    public static int getMultiIncrement(PokerHand.HandType handType) {
        PlanetCard card = CARDS.get(handType);
        return card != null ? card.multiIncrement : 0;
    }

    // Apply level-up for this card's hand type
    public void apply() {
        LEVELS.put(handType, LEVELS.getOrDefault(handType, 1) + 1);
    }

    // Getters
    public String getName() {
        return name;
    }

    public int getChipIncrement() {
        return chipIncrement;
    }

    public int getMultiIncrement() {
        return multiIncrement;
    }

    public PokerHand.HandType getHandType() {
        return handType;
    }

    /*
    To level up a hand: PlanetCard.getForHand(HandType.HIGH_CARD).apply();
    To get current stats: PlanetCard.getLevel(HandType.HIGH_CARD)

    // Level up "High Card" using Pluto
    PlanetCard pluto = PlanetCard.getForHand(PokerHand.HandType.HIGH_CARD);
    pluto.apply();

    // Create a High Card poker hand
    PokerHand highCard = new PokerHand(PokerHand.HandType.HIGH_CARD);
    System.out.println(highCard.getChips()); // 5 + (2-1)*10 = 15
    System.out.println(highCard.getMultiplier()); // 1 + (2-1)*1 = 2
    */
}
