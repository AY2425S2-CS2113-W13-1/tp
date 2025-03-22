package Javatro;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import Javatro.Core.Deck;
import Javatro.Core.JavatroException;
import Javatro.Core.Round;

import org.junit.jupiter.api.Test;

import java.util.List;

public class RoundTest {
    private void assertRoundInitialization(int blindScore, int remainingPlays)
            throws JavatroException {
        Deck deck = new Deck();
        Round round = new Round(blindScore, remainingPlays, deck, "", "");
        assertEquals(blindScore, round.getBlindScore());
        assertEquals(remainingPlays, round.getRemainingPlays());
        assertEquals(0, round.getCurrentScore());
        assertEquals(3, round.getRemainingDiscards());
        assertEquals(false, round.isRoundOver());
    }

    private void assertRoundInitializationFailure(
            int blindScore, int remainingPlays, Deck deck, String expectedMessage) {
        try {
            new Round(blindScore, remainingPlays, deck, "", "");
            fail();
        } catch (JavatroException e) {
            assertEquals(expectedMessage, e.getMessage());
        }
    }

    private void assertRoundOverAfterPlays(
            int blindScore,
            int totalPlays,
            int playsToMake,
            boolean expectedIsOver,
            boolean expectedIsWon)
            throws JavatroException {
        Deck deck = new Deck();
        Round round = new Round(blindScore, totalPlays, deck, "", "");

        for (int i = 0; i < playsToMake; i++) {
            round.playCards(List.of(0, 1, 2, 3, 4));
        }

        assertEquals(expectedIsOver, round.isRoundOver());
        if (expectedIsWon) {
            assertEquals(expectedIsWon, round.isWon());
        }
    }

    private void assertPlayCardsFails(
            int blindScore, int remainingPlays, int playsToMake, String expectedErrorMessage)
            throws JavatroException {

        try {
            Deck deck = new Deck();
            Round round = new Round(blindScore, remainingPlays, deck, "", "");

            // Make the specified number of valid plays
            for (int i = 0; i < playsToMake; i++) {
                round.playCards(List.of(0, 1, 2, 3, 4));
            }

            // Attempt one more play which should fail
            round.playCards(List.of(0, 1, 2, 3, 4));
            fail();
        } catch (JavatroException e) {
            assertEquals(expectedErrorMessage, e.getMessage());
        }
    }

    private void assertPlayCardsInvalidHandSize(
            int blindScore,
            int remainingPlays,
            List<Integer> cardIndices,
            String expectedErrorMessage)
            throws JavatroException {
        Deck deck = new Deck();
        Round round = new Round(blindScore, remainingPlays, deck, "", "");

        try {
            round.playCards(cardIndices);
            fail();
        } catch (JavatroException e) {
            assertEquals(expectedErrorMessage, e.getMessage());
        }
    }

    private void assertRoundNotOver(int blindScore, int remainingPlays, int playsToMake)
            throws JavatroException {
        Deck deck = new Deck();
        Round round = new Round(blindScore, remainingPlays, deck, "", "");

        for (int i = 0; i < playsToMake; i++) {
            round.playCards(List.of(0, 1, 2, 3, 4));
        }

        assertEquals(false, round.isRoundOver());
        assertEquals(false, round.isWon());
    }

    @Test
    public void round_correctInitialization_success() throws JavatroException {
        assertRoundInitialization(100, 3);
        assertRoundInitialization(200, 5);
        assertRoundInitialization(300, 7);
        assertRoundInitialization(300, 7);
        assertRoundInitialization(0, 1);
    }

    @Test
    public void round_incorrectInitializatioin() throws JavatroException {
        assertRoundInitializationFailure(
                100, 0, new Deck(), "Number of plays per round must be greater than 0");
        assertRoundInitializationFailure(
                -100, 3, new Deck(), "Blind score must be greater than or equal to 0");
        assertRoundInitializationFailure(100, 3, null, "Deck cannot be null");
        assertRoundInitializationFailure(
                -100, 0, new Deck(), "Blind score must be greater than or equal to 0");
        assertRoundInitializationFailure(
                -100, 3, null, "Blind score must be greater than or equal to 0");
        assertRoundInitializationFailure(
                100, 0, null, "Number of plays per round must be greater than 0");
        assertRoundInitializationFailure(
                -100, 0, null, "Blind score must be greater than or equal to 0");
    }

    @Test
    public void round_playCards_roundNotOver() throws JavatroException {
        // Test with regular blind score and plays
        assertRoundNotOver(100, 3, 1);

        // Test with high blind score
        assertRoundNotOver(1000, 3, 1);

        // Test with many remaining plays
        assertRoundNotOver(100, 3000, 5);
    }

    @Test
    public void round_playCards_roundOver() throws JavatroException {
        // Round is over after using all plays
        assertRoundOverAfterPlays(99999, 3, 3, true, false);
        assertRoundOverAfterPlays(99999, 5, 5, true, false);
        assertRoundOverAfterPlays(99999, 8, 8, true, false);

        // Round is over and won when blind score is 0
        assertRoundOverAfterPlays(0, 1, 1, true, true);
    }

    @Test
    public void round_playCards_tooManyPlays() throws JavatroException {
        // Test with 3 plays
        assertPlayCardsFails(100, 3, 3, "No plays remaining");

        // Test with 5 plays
        assertPlayCardsFails(100, 2, 2, "No plays remaining");

        // Test with 0 plays
        assertPlayCardsFails(100, 0, 0, "Number of plays per round must be greater than 0");
    }

    @Test
    public void round_playCards_invalidHandSize() throws JavatroException {
        assertPlayCardsInvalidHandSize(
                100,
                3,
                List.of(0, 1, 2, 3, 4, 5),
                "A poker hand must contain between 1 and 5 cards.");
        assertPlayCardsInvalidHandSize(
                100, 3, List.of(0, 1, 2, 3), "A poker hand must contain between 1 and 5 cards.");
        assertPlayCardsInvalidHandSize(
                100,
                3,
                List.of(0, 1, 2, 3, 4, 5, 6),
                "A poker hand must contain between 1 and 5 cards.");
        assertPlayCardsInvalidHandSize(
                100,
                3,
                List.of(0, 1, 2, 3, 4, 5, 6, 7),
                "A poker hand must contain between 1 and 5 cards.");
        // Test with 0 cards
        assertPlayCardsInvalidHandSize(
                100, 3, List.of(), "A poker hand must contain between 1 and 5 cards.");
    }

    @Test
    public void round_discardCards_success() throws JavatroException {
        Deck deck = new Deck();
        Round round = new Round(100, 3, deck, "", "");

        // Initial state
        assertEquals(3, round.getRemainingDiscards());
        int initialHandSize = round.getPlayerHand().size();

        // Discard 2 cards
        round.discardCards(List.of(0, 1));

        // Check state after discard
        assertEquals(2, round.getRemainingDiscards());
        assertEquals(
                initialHandSize, round.getPlayerHand().size()); // Hand size should remain the same
    }

    @Test
    public void round_discardCards_tooManyDiscards() throws JavatroException {
        Deck deck = new Deck();
        Round round = new Round(100, 3, deck, "", "");

        // Use all 3 discards
        round.discardCards(List.of(0));
        round.discardCards(List.of(0));
        round.discardCards(List.of(0));

        // Fourth discard should fail
        try {
            round.discardCards(List.of(0));
            fail("Should have thrown an exception for too many discards");
        } catch (IllegalStateException e) {
            assertEquals("No remaining discards available", e.getMessage());
        }
    }

    @Test
    public void round_emptyDiscardList() throws JavatroException {
        Deck deck = new Deck();
        Round round = new Round(100, 3, deck, "", "");

        // Initial state
        assertEquals(3, round.getRemainingDiscards());
        int initialHandSize = round.getPlayerHand().size();

        // Discard 0 cards
        round.discardCards(List.of());

        // Should still use a discard
        assertEquals(2, round.getRemainingDiscards());
        assertEquals(initialHandSize, round.getPlayerHand().size());
    }

    @Test
    public void round_setNameAndDescription() throws JavatroException {
        Deck deck = new Deck();
        Round round = new Round(100, 3, deck, "", "");

        // Set new values
        round.setRoundName("New Round");
        round.setRoundDescription("New Description");

        // Check values were updated
        assertEquals("New Round", round.getRoundName());
        assertEquals("New Description", round.getRoundDescription());
    }
}
