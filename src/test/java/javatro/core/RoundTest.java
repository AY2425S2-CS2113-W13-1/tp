package javatro.core;

import static javatro.core.Deck.DeckType.DEFAULT;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;

import javatro.core.jokers.HeldJokers;
import javatro.display.UI;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

public class RoundTest {
    private static String getExceptionMessage(String message) {
        return UI.RED + message + UI.END;
    }

    private static final String INVALIDPLAYEDHANDERROR =
            "A poker hand must contain between 1 and 5 cards.";
    private static final String INVALIDPLAYSPERROUND =
            "Number of plays per round must be greater than 0.";
    private static final String INVALIDDECK = "Deck cannot be null.";
    private static final String INVALIDPLAYSREMAINING = "No plays remaining.";

    enum isWon {
        WON,
        LOST,
        UNKNOWN
    }

    private static HeldJokers heldJokers;

    @BeforeAll
    public static void init() {
        heldJokers = new HeldJokers();
    }

    private void assertRoundInitialization(int anteCount, Ante.Blind blind, int remainingPlays)
            throws JavatroException {
        Deck deck = new Deck(Deck.DeckType.DEFAULT);
        Ante ante = new Ante();
        ante.setAnteCount(anteCount);
        ante.setBlind(blind);
        int expectedBlindScore = ante.getRoundScore();
        Round round = new Round(ante, remainingPlays, deck, heldJokers, "", "");
        assertEquals(expectedBlindScore, round.getBlindScore());
        assertEquals(remainingPlays, round.getRemainingPlays());
        assertEquals(0, round.getCurrentScore());
        assertEquals(4, round.getRemainingDiscards());
        assertFalse(round.isRoundOver());
    }

    private void assertRoundInitializationFailure(
            int anteCount,
            Ante.Blind blind,
            int remainingPlays,
            Deck deck,
            String expectedMessage) {
        try {
            Ante ante = new Ante();
            ante.setAnteCount(anteCount);
            ante.setBlind(blind);
            new Round(ante, remainingPlays, deck, heldJokers, "", "");
            fail();
        } catch (JavatroException e) {
            assertEquals(getExceptionMessage(expectedMessage), e.getMessage());
        }
    }

    private void assertRoundOverAfterPlays(
            int blindScore, int totalPlays, int playsToMake, boolean expectedIsOver)
            throws JavatroException {
        Deck deck = new Deck(Deck.DeckType.DEFAULT);
        Ante ante = new Ante();
        ante.setAnteCount(1); // Default setting
        ante.setBlind(Ante.Blind.SMALL_BLIND); // Default setting
        Round round = new Round(ante, totalPlays, deck, heldJokers, "", "");

        for (int i = 0; i < playsToMake; i++) {
            round.playCards(List.of(0, 1, 2, 3, 4));
        }

        assertEquals(expectedIsOver, round.isRoundOver());
    }

    private void assertPlayCardsFails(
            int anteCount,
            Ante.Blind blind,
            int remainingPlays,
            int playsToMake,
            String expectedErrorMessage)
            throws JavatroException {
        try {
            Deck deck = new Deck(Deck.DeckType.DEFAULT);
            Ante ante = new Ante();
            ante.setAnteCount(anteCount);
            ante.setBlind(blind);
            Round round = new Round(ante, remainingPlays, deck, heldJokers, "", "");

            // Make the specified number of valid plays
            for (int i = 0; i < playsToMake; i++) {
                round.playCards(List.of(0, 1, 2, 3, 4));
            }

            // Attempt one more play which should fail
            round.playCards(List.of(0, 1, 2, 3, 4));
            fail();
        } catch (JavatroException e) {
            assertEquals(getExceptionMessage(expectedErrorMessage), e.getMessage());
        }
    }

    private void assertPlayCardsInvalidHandSize(
            int anteCount,
            Ante.Blind blind,
            int remainingPlays,
            List<Integer> cardIndices,
            String expectedErrorMessage)
            throws JavatroException {
        Deck deck = new Deck(Deck.DeckType.DEFAULT);
        Ante ante = new Ante();
        ante.setAnteCount(anteCount);
        ante.setBlind(blind);
        Round round = new Round(ante, remainingPlays, deck, heldJokers, "", "");
        try {
            round.playCards(cardIndices);
            fail();
        } catch (JavatroException e) {
            assertEquals(getExceptionMessage(expectedErrorMessage), e.getMessage());
        }
    }

    private void assertRoundOver(
            int anteCount,
            Ante.Blind blind,
            int remainingPlays,
            int playsToMake,
            boolean expectedIsOver)
            throws JavatroException {
        Deck deck = new Deck(Deck.DeckType.DEFAULT);
        Ante ante = new Ante();
        ante.setAnteCount(anteCount);
        ante.setBlind(blind);
        Round round = new Round(ante, remainingPlays, deck, heldJokers, "", "");

        for (int i = 0; i < playsToMake; i++) {
            round.playCards(List.of(0, 1, 2, 3, 4));
        }

        assertEquals(expectedIsOver, round.isRoundOver());
    }

    @Test
    public void round_correctInitialization_success() throws JavatroException {
        assertRoundInitialization(1, Ante.Blind.SMALL_BLIND, 3);
        assertRoundInitialization(2, Ante.Blind.SMALL_BLIND, 5);
        assertRoundInitialization(2, Ante.Blind.LARGE_BLIND, 7);
        assertRoundInitialization(3, Ante.Blind.BOSS_BLIND, 1);
    }

    @Test
    public void round_incorrectInitializatioin() throws JavatroException {
        assertRoundInitializationFailure(
                1, Ante.Blind.SMALL_BLIND, 0, new Deck(DEFAULT), INVALIDPLAYSPERROUND);
        assertRoundInitializationFailure(1, Ante.Blind.SMALL_BLIND, 3, null, INVALIDDECK);
    }

    @Test
    public void round_playCards_roundNotOver() throws JavatroException {
        // Test with first blind score and plays
        assertRoundOver(1, Ante.Blind.SMALL_BLIND, 3, 1, false);
        // Test with high blind score
        assertRoundOver(1, Ante.Blind.LARGE_BLIND, 10000, 1, false);

        // Test with high ante count and plays
        assertRoundOver(8, Ante.Blind.SMALL_BLIND, 10000, 1, false);
        // Test with many remaining plays
        assertRoundOver(8, Ante.Blind.LARGE_BLIND, 10000, 1, false);

        // Test won
        assertRoundOver(1, Ante.Blind.SMALL_BLIND, 10000, 8, true);
    }

    @Test
    public void round_playCards_roundOver() throws JavatroException {
        Ante ante = new Ante();
        ante.setAnteCount(8);
        ante.setBlind(Ante.Blind.SMALL_BLIND);
        assertRoundOverAfterPlays(ante.getRoundScore(), 3, 3, true);

        ante = new Ante();
        ante.setAnteCount(8);
        ante.setBlind(Ante.Blind.BOSS_BLIND);
        assertRoundOverAfterPlays(ante.getRoundScore(), 5, 5, true);

        ante = new Ante();
        ante.setAnteCount(8);
        ante.setBlind(Ante.Blind.BOSS_BLIND);
        assertRoundOverAfterPlays(ante.getRoundScore(), 8, 8, true);

        ante = new Ante();
        ante.setAnteCount(1);
        ante.setBlind(Ante.Blind.SMALL_BLIND);
        assertRoundOverAfterPlays(ante.getRoundScore(), 1, 1, true);
    }

    @Test
    public void round_playCards_tooManyPlays() throws JavatroException {
        // Test with 3 plays
        assertPlayCardsFails(1, Ante.Blind.SMALL_BLIND, 3, 3, INVALIDPLAYSREMAINING);

        // Test with 5 plays
        assertPlayCardsFails(1, Ante.Blind.SMALL_BLIND, 2, 2, INVALIDPLAYSREMAINING);
        // Test with 0 plays
        assertPlayCardsFails(1, Ante.Blind.SMALL_BLIND, 0, 0, INVALIDPLAYSPERROUND);
    }

    @Test
    public void round_playCards_invalidHandSize() throws JavatroException {
        assertPlayCardsInvalidHandSize(
                1, Ante.Blind.SMALL_BLIND, 3, List.of(0, 1, 2, 3, 4, 5), INVALIDPLAYEDHANDERROR);
        assertPlayCardsInvalidHandSize(
                1, Ante.Blind.SMALL_BLIND, 3, List.of(0, 1, 2, 3, 4, 5, 6), INVALIDPLAYEDHANDERROR);
        assertPlayCardsInvalidHandSize(
                1,
                Ante.Blind.SMALL_BLIND,
                3,
                List.of(0, 1, 2, 3, 4, 5, 6, 7),
                INVALIDPLAYEDHANDERROR);
        // Test with 0 cards
        assertPlayCardsInvalidHandSize(
                1, Ante.Blind.SMALL_BLIND, 3, List.of(), INVALIDPLAYEDHANDERROR);
    }

    @Test
    public void round_discardCards_success() throws JavatroException {
        Deck deck = new Deck(Deck.DeckType.DEFAULT);
        Ante ante = new Ante();
        ante.setAnteCount(1);
        ante.setBlind(Ante.Blind.SMALL_BLIND);
        Round round = new Round(ante, 3, deck, heldJokers, "", "");

        // Initial state
        assertEquals(4, round.getRemainingDiscards());
        int initialHandSize = round.getPlayerHand().size();

        // Discard 2 cards
        round.discardCards(List.of(0, 1));

        // Check state after discard
        assertEquals(3, round.getRemainingDiscards());
        assertEquals(initialHandSize, round.getPlayerHand().size());
    }

    @Test
    public void round_discardCards_tooManyDiscards() throws JavatroException {
        Deck deck = new Deck(Deck.DeckType.DEFAULT);
        Ante ante = new Ante();
        ante.setAnteCount(1);
        ante.setBlind(Ante.Blind.SMALL_BLIND);
        Round round = new Round(ante, 3, deck, heldJokers, "", "");

        // Use all 4 discards
        round.discardCards(List.of(0));
        round.discardCards(List.of(0));
        round.discardCards(List.of(0));
        round.discardCards(List.of(0));

        // Fifth discard should fail
        try {
            round.discardCards(List.of(0));
            fail("Should have thrown an exception for too many discards");
        } catch (JavatroException e) {
            assertEquals(getExceptionMessage("No remaining discards available"), e.getMessage());
        }
    }

    @Test
    public void round_emptyDiscardList() throws JavatroException {
        Deck deck = new Deck(Deck.DeckType.DEFAULT);
        Ante ante = new Ante();
        ante.setAnteCount(1);
        ante.setBlind(Ante.Blind.SMALL_BLIND);
        Round round = new Round(ante, 3, deck, heldJokers, "", "");

        // Initial state
        assertEquals(4, round.getRemainingDiscards());
        int initialHandSize = round.getPlayerHand().size();

        // Discard 0 cards
        try {
            round.discardCards(List.of());
            fail("Should have thrown an exception for discarding zero cards");
        } catch (JavatroException e) {
            assertEquals(getExceptionMessage("Cannot discard zero cards"), e.getMessage());
        }

        assertEquals(4, round.getRemainingDiscards());
        assertEquals(initialHandSize, round.getPlayerHand().size());
    }

    @Test
    public void round_setNameAndDescription() throws JavatroException {
        Deck deck = new Deck(Deck.DeckType.DEFAULT);
        Ante ante = new Ante();
        ante.setAnteCount(1);
        ante.setBlind(Ante.Blind.SMALL_BLIND);
        Round round = new Round(ante, 3, deck, heldJokers, "", "");

        // Set new values
        round.setRoundName("New Round");
        round.setRoundDescription("New Description");

        // Check values were updated
        assertEquals("New Round", round.getRoundName());
        assertEquals("New Description", round.getRoundDescription());
    }
}
