package javatro.core.round;

import static javatro.core.Deck.DeckType.DEFAULT;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;

import javatro.core.Ante;
import javatro.core.BossType;
import javatro.core.Deck;
import javatro.core.JavatroException;
import javatro.core.jokers.HeldJokers;
import javatro.display.UI;
import javatro.storage.Storage;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

public class RoundTest {

    private static final String INVALIDPLAYEDHANDERROR =
            "A poker hand must contain between 1 and 5 cards.";
    private static final String INVALIDPLAYSPERROUND =
            "Number of plays per round must be greater than 0.";
    private static final String INVALIDDECK = "Deck cannot be null.";
    private static HeldJokers heldJokers;

    enum isWon {
        WON,
        LOST,
        UNKNOWN
    }

    private static String getExceptionMessage(String message) {
        return UI.RED + message + UI.END;
    }

    @BeforeAll
    public static void init() {
        Storage.saveActive = false;
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
        if (ante.getBlind() != Ante.Blind.BOSS_BLIND) {
            assertEquals(3, round.getRemainingDiscards());
        }
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

    private void testRoundInitialization(int anteCount, Ante.Blind blind, int remainingPlays)
            throws JavatroException {
        assertRoundInitialization(anteCount, blind, remainingPlays);
    }

    private void testRoundPlayCards(
            int anteCount,
            Ante.Blind blind,
            int remainingPlays,
            int playsToMake,
            boolean expectedIsOver)
            throws JavatroException {
        assertRoundOver(anteCount, blind, remainingPlays, playsToMake, expectedIsOver);
    }

    private void testRoundOver(int anteCount, Ante.Blind blind, int totalPlays, int playsToMake)
            throws JavatroException {
        Ante ante = new Ante();
        ante.setAnteCount(anteCount);
        ante.setBlind(blind);
        assertRoundOverAfterPlays(ante.getRoundScore(), totalPlays, playsToMake, true);
    }

    private void testPlayCardsFails(
            int anteCount,
            Ante.Blind blind,
            int remainingPlays,
            int playsToMake,
            String expectedError)
            throws JavatroException {
        assertPlayCardsFails(anteCount, blind, remainingPlays, playsToMake, expectedError);
    }

    private void testBossType(BossType bossType) throws JavatroException {
        Deck deck = new Deck(Deck.DeckType.DEFAULT);
        Ante ante = new Ante();
        ante.setAnteCount(1);
        ante.setBlind(Ante.Blind.BOSS_BLIND);
        Round round = new Round(ante, 3, deck, heldJokers, bossType);

        // Check that default values are maintained for other bosses
        assertEquals(RoundConfig.DEFAULT_MIN_HAND_SIZE, round.getConfig().getMinHandSize());
        assertEquals(RoundConfig.DEFAULT_MAX_HAND_SIZE, round.getConfig().getMaxHandSize());
    }

    @Test
    public void roundcorrectInitializationsmallBlind1() throws JavatroException {
        testRoundInitialization(1, Ante.Blind.SMALL_BLIND, 3);
    }

    @Test
    public void roundcorrectInitializationsmallBlind2() throws JavatroException {
        testRoundInitialization(2, Ante.Blind.SMALL_BLIND, 5);
    }

    @Test
    public void roundcorrectInitializationlargeBlind() throws JavatroException {
        testRoundInitialization(2, Ante.Blind.LARGE_BLIND, 7);
    }

    @Test
    public void roundcorrectInitializationbossBlind() throws JavatroException {
        testRoundInitialization(3, Ante.Blind.BOSS_BLIND, 1);
    }

    @Test
    public void roundincorrectInitializatioin() throws JavatroException {
        assertRoundInitializationFailure(
                1, Ante.Blind.SMALL_BLIND, 0, new Deck(DEFAULT), INVALIDPLAYSPERROUND);
        assertRoundInitializationFailure(1, Ante.Blind.SMALL_BLIND, 3, null, INVALIDDECK);
    }

    @Test
    public void roundplayCards_smallBlindfewPlays() throws JavatroException {
        testRoundPlayCards(1, Ante.Blind.SMALL_BLIND, 3, 1, false);
    }

    @Test
    public void roundplayCards_largeBlindfewPlays() throws JavatroException {
        testRoundPlayCards(1, Ante.Blind.LARGE_BLIND, 10000, 1, false);
    }

    @Test
    public void roundplayCards_smallBlindhighAnte() throws JavatroException {
        testRoundPlayCards(8, Ante.Blind.SMALL_BLIND, 10000, 1, false);
    }

    @Test
    public void roundplayCards_largeBlindhighAnte() throws JavatroException {
        testRoundPlayCards(8, Ante.Blind.LARGE_BLIND, 10000, 1, false);
    }

    @Test
    public void roundplayCards_smallBlindmanyPlays() throws JavatroException {
        testRoundPlayCards(1, Ante.Blind.SMALL_BLIND, 10000, 8, true);
    }

    @Test
    public void roundplayCards_roundOversmallBlind() throws JavatroException {
        testRoundOver(8, Ante.Blind.SMALL_BLIND, 3, 3);
    }

    @Test
    public void roundplayCards_roundOverbossBlind1() throws JavatroException {
        testRoundOver(8, Ante.Blind.BOSS_BLIND, 5, 5);
    }

    @Test
    public void roundplayCards_roundOverbossBlind2() throws JavatroException {
        testRoundOver(8, Ante.Blind.BOSS_BLIND, 8, 8);
    }

    @Test
    public void roundplayCards_roundOversmallBlind_exactPlays() throws JavatroException {
        testRoundOver(1, Ante.Blind.SMALL_BLIND, 1, 1);
    }

    @Test
    public void roundplayCards_tooManyPlayscase1() throws JavatroException {
        testPlayCardsFails(1, Ante.Blind.SMALL_BLIND, 3, 3, "No plays remaining.");
    }

    @Test
    public void roundplayCards_tooManyPlayscase2() throws JavatroException {
        testPlayCardsFails(
                1,
                Ante.Blind.SMALL_BLIND,
                0,
                0,
                "Number of plays per round must be greater than 0.");
    }

    @Test
    public void roundplayCardsinvalidHandSize() throws JavatroException {
        // Test with too many cards
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
    public void rounddiscardCardssuccess() throws JavatroException {
        Deck deck = new Deck(Deck.DeckType.DEFAULT);
        Ante ante = new Ante();
        ante.setAnteCount(1);
        ante.setBlind(Ante.Blind.SMALL_BLIND);
        Round round = new Round(ante, 3, deck, heldJokers, "", "");

        // Initial state
        assertEquals(3, round.getRemainingDiscards());
        int initialHandSize = round.getPlayerHandCards().size();

        // Discard 2 cards
        round.discardCards(List.of(0, 1));

        // Check state after discard
        assertEquals(2, round.getRemainingDiscards());
        assertEquals(initialHandSize, round.getPlayerHandCards().size());
    }

    @Test
    public void rounddiscardCardstooManyDiscards() throws JavatroException {
        Deck deck = new Deck(Deck.DeckType.DEFAULT);
        Ante ante = new Ante();
        ante.setAnteCount(1);
        ante.setBlind(Ante.Blind.SMALL_BLIND);
        Round round = new Round(ante, 3, deck, heldJokers, "", "");

        // Use all 3 discards
        round.discardCards(List.of(0));
        round.discardCards(List.of(0));
        round.discardCards(List.of(0));

        // Fourth discard should fail
        try {
            round.discardCards(List.of(0));
            fail("Should have thrown an exception for too many discards");
        } catch (JavatroException e) {
            assertEquals(getExceptionMessage("No remaining discards available"), e.getMessage());
        }
    }

    @Test
    public void roundemptyDiscardList() throws JavatroException {
        Deck deck = new Deck(Deck.DeckType.DEFAULT);
        Ante ante = new Ante();
        ante.setAnteCount(1);
        ante.setBlind(Ante.Blind.SMALL_BLIND);
        Round round = new Round(ante, 3, deck, heldJokers, "", "");

        // Initial state
        assertEquals(3, round.getRemainingDiscards());
        int initialHandSize = round.getPlayerHandCards().size();

        // Discard 0 cards
        try {
            round.discardCards(List.of());
            fail("Should have thrown an exception for discarding zero cards");
        } catch (JavatroException e) {
            assertEquals(getExceptionMessage("Cannot discard zero cards"), e.getMessage());
        }

        assertEquals(3, round.getRemainingDiscards());
        assertEquals(initialHandSize, round.getPlayerHandCards().size());
    }

    @Test
    public void roundsetNameAndDescription() throws JavatroException {
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

    @Test
    public void roundbossTypespecificBehaviors() throws JavatroException {
        // Setup
        Deck deck = new Deck(Deck.DeckType.DEFAULT);
        Ante ante = new Ante();
        ante.setAnteCount(1);
        ante.setBlind(Ante.Blind.BOSS_BLIND);

        // Test THE_NEEDLE
        Round needleRound = new Round(ante, 3, deck, heldJokers, BossType.THE_NEEDLE);
        assertEquals(RoundConfig.DEFAULT_MIN_HAND_SIZE, needleRound.getConfig().getMinHandSize());
        assertEquals(RoundConfig.DEFAULT_MAX_HAND_SIZE, needleRound.getConfig().getMaxHandSize());

        // Test THE_WATER
        Round waterRound = new Round(ante, 3, deck, heldJokers, BossType.THE_WATER);
        assertEquals(RoundConfig.DEFAULT_MIN_HAND_SIZE, waterRound.getConfig().getMinHandSize());
        assertEquals(RoundConfig.DEFAULT_MAX_HAND_SIZE, waterRound.getConfig().getMaxHandSize());

        // Test THE_PSYCHIC
        Round psychicRound = new Round(ante, 3, deck, heldJokers, BossType.THE_PSYCHIC);
        assertEquals(3, psychicRound.getRemainingPlays());
        assertEquals(5, psychicRound.getConfig().getMaxHandSize());
    }

    @Test
    public void roundbossTypetheClub() throws JavatroException {
        testBossType(BossType.THE_CLUB);
    }

    @Test
    public void roundbossTypetheWindow() throws JavatroException {
        testBossType(BossType.THE_WINDOW);
    }

    @Test
    public void roundbossTypetheHead() throws JavatroException {
        testBossType(BossType.THE_HEAD);
    }

    @Test
    public void roundbossTypetheGoad() throws JavatroException {
        testBossType(BossType.THE_GOAD);
    }

    @Test
    public void roundbossTypethePlant() throws JavatroException {
        testBossType(BossType.THE_PLANT);
    }

    @Test
    public void roundtestRandomBossType() throws JavatroException {
        Deck deck = new Deck(Deck.DeckType.DEFAULT);
        Ante ante = new Ante();
        ante.setAnteCount(1);
        ante.setBlind(Ante.Blind.BOSS_BLIND);
        Round round = new Round(ante, 3, deck, heldJokers, "", "");

        switch (round.getBossType()) {
        case THE_NEEDLE:
            assertEquals(BossType.THE_NEEDLE, round.getBossType());
            assertEquals(RoundConfig.DEFAULT_MIN_HAND_SIZE, round.getConfig().getMinHandSize());
            break;
        case THE_WATER:
            assertEquals(BossType.THE_WATER, round.getBossType());
            assertEquals(0, round.getRemainingDiscards());
            assertEquals(RoundConfig.DEFAULT_MIN_HAND_SIZE, round.getConfig().getMinHandSize());
            break;
        case THE_PSYCHIC:
            assertEquals(BossType.THE_PSYCHIC, round.getBossType());
            assertEquals(5, round.getConfig().getMaxHandSize());
            break;
        default:
            assertEquals(RoundConfig.DEFAULT_MAX_HAND_SIZE, round.getConfig().getMaxHandSize());
            assertEquals(RoundConfig.DEFAULT_MIN_HAND_SIZE, round.getConfig().getMinHandSize());
            break;
        }
    }

    @Test
    public void roundtestDeckVariants() throws JavatroException {
        // Test RED deck variant (increases remaining discards)
        Deck redDeck = new Deck(Deck.DeckType.RED);
        Ante ante = new Ante();
        ante.setAnteCount(1);
        ante.setBlind(Ante.Blind.SMALL_BLIND);
        Round redRound = new Round(ante, 3, redDeck, heldJokers, "", "");
        assertEquals(4, redRound.getRemainingDiscards()); // Default 3 + 1 more from RED deck

        // Test BLUE deck variant (increases remaining plays)
        Deck blueDeck = new Deck(Deck.DeckType.BLUE);
        Round blueRound = new Round(ante, 3, blueDeck, heldJokers, "", "");
        assertEquals(4, blueRound.getRemainingPlays()); // Initial 3 + 1 more from BLUE deck

        // Test DEFAULT deck variant (no changes to discards or plays)
        Deck deck = new Deck(Deck.DeckType.DEFAULT);
        Round greenRound = new Round(ante, 3, deck, heldJokers, "", "");
        assertEquals(3, greenRound.getRemainingDiscards()); // Default value unchanged
        assertEquals(3, greenRound.getRemainingPlays()); // Default value unchanged
    }
}
