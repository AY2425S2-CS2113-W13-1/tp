package javatro.core;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

// @author swethaiscool
/**
 * Test class for the {@link Ante} class. This class tests the functionality of the Ante class,
 * including the initialization of values, progression between rounds, blind multipliers, and score
 * calculations.
 */
public class AnteTest {
    private Ante ante;
    private Random random;
    /** Initializes a new Ante object before each test. */
    @BeforeEach
    void setUp() {
        ante = new Ante();
        random = new Random();
    }

    /**
     * Tests the initial values of the Ante object. Ensures the Ante count starts at 1, the blind is
     * set to SMALL_BLIND, and the round score is set to 300.
     */
    @Test
    void testInitialValues() {
        assertEquals(1, ante.getAnteCount(), "Ante count should start at 1");
        assertEquals(
                Ante.Blind.SMALL_BLIND, ante.getBlind(), "Initial blind should be SMALL_BLIND");
        assertEquals(300, ante.getRoundScore(), "Initial round score should be 300");
    }

    /**
     * Tests the multipliers for different blinds. Verifies that each blind (SMALL_BLIND,
     * LARGE_BLIND, BOSS_BLIND) has the expected multiplier.
     */
    @Test
    void testBlindMultiplier() {
        ante.setBlind(Ante.Blind.SMALL_BLIND);
        assertEquals(
                1.0,
                ante.getBlind().getMultiplier(),
                "SMALL_BLIND should have a multiplier of 1.0");

        ante.setBlind(Ante.Blind.LARGE_BLIND);
        assertEquals(
                1.5,
                ante.getBlind().getMultiplier(),
                "LARGE_BLIND should have a multiplier of 1.5");

        ante.setBlind(Ante.Blind.BOSS_BLIND);
        assertEquals(
                2.0, ante.getBlind().getMultiplier(), "BOSS_BLIND should have a multiplier of 2.0");
    }

    /**
     * Tests the progression of the blind and ante count across rounds. Verifies that after each
     * round, the blind changes in the expected order, and the ante count increments correctly,
     * resetting after reaching a cap.
     */
    @Test
    void testNextRoundProgression() {
        ante.nextRound();
        assertEquals(Ante.Blind.LARGE_BLIND, ante.getBlind(), "Blind should change to LARGE_BLIND");

        ante.nextRound();
        assertEquals(Ante.Blind.BOSS_BLIND, ante.getBlind(), "Blind should change to BOSS_BLIND");

        ante.nextRound();
        assertEquals(2, ante.getAnteCount(), "Ante count should increment");
        assertEquals(Ante.Blind.SMALL_BLIND, ante.getBlind(), "Blind should reset to SMALL_BLIND");
    }

    /**
     * Tests the calculation of the round score based on the current blind. Verifies that the score
     * is calculated correctly for each blind.
     */
    @Test
    void testRoundScoreCalculation() {
        ante.setBlind(Ante.Blind.SMALL_BLIND);
        assertEquals(300, ante.getRoundScore(), "Score for SMALL_BLIND should be correct");

        ante.setBlind(Ante.Blind.LARGE_BLIND);
        assertEquals(450, ante.getRoundScore(), "Score for LARGE_BLIND should be correct");

        ante.setBlind(Ante.Blind.BOSS_BLIND);
        assertEquals(600, ante.getRoundScore(), "Score for BOSS_BLIND should be correct");
    }

    /**
     * Tests the maximum ante count cap. Ensures that the ante count does not exceed 8 after several
     * rounds.
     */
    @Test
    void testMaxAnteCap() {
        for (int i = 0; i < 8; i++) {
            ante.nextRound();
            ante.nextRound();
            ante.nextRound();
        }
        assertEquals(8, ante.getAnteCount(), "Ante count should not exceed 8");
    }

    /**
     * Tests random blinds and their respective scores. Verifies that for each randomly selected
     * blind and ante count, the score matches the expected value based on the blind's multiplier.
     */
    @Test
    void testRandomBlindsAndScores() {
        // Test for 10 random iterations
        for (int i = 0; i < 10; i++) {
            // Randomly select ante count (from 1 to 8)
            int randomAnteCount = random.nextInt(8) + 1;

            // Move to the selected ante count using nextRound
            for (int j = 1; j < randomAnteCount; j++) {
                ante.nextRound();
            }

            // Randomly select a blind
            int blindIndex = random.nextInt(Ante.Blind.values().length);
            Ante.Blind randomBlind = Ante.Blind.values()[blindIndex];
            ante.setBlind(randomBlind);

            // Get expected score
            int expectedScore = (int) (ante.getAnteScore() * randomBlind.getMultiplier());

            // Assert the score is correct
            assertEquals(
                    expectedScore,
                    ante.getRoundScore(),
                    "Random score calculation failed for Ante "
                            + randomAnteCount
                            + " and Blind "
                            + randomBlind.getName());

            // Reset the ante for the next iteration
            ante.resetAnte();
        }
    }
}
