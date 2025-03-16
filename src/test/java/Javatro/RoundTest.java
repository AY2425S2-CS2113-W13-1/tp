// package Javatro;
//
// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.fail;
//
// import org.junit.jupiter.api.Test;
//
// public class RoundTest {
//
//    @Test
//    public void round_correctInitialization_success() throws JavatroException {
//        State state = new State(100, 3, new Deck());
//        Round round = new Round(state);
//        assertEquals(100, round.getBlindScore());
//        assertEquals(3, round.getRemainingPlays());
//        assertEquals(0, round.getCurrentScore());
//        assertEquals(3, round.getRemainingDiscards());
//
//        state = new State(200, 5, new Deck());
//        round = new Round(state);
//        assertEquals(200, round.getBlindScore());
//        assertEquals(5, round.getRemainingPlays());
//        assertEquals(0, round.getCurrentScore());
//        assertEquals(3, round.getRemainingDiscards());
//
//        state = new State(300, 7, new Deck());
//        round = new Round(state);
//        assertEquals(300, round.getBlindScore());
//        assertEquals(7, round.getRemainingPlays());
//        assertEquals(0, round.getCurrentScore());
//        assertEquals(3, round.getRemainingDiscards());
//
//        state = new State(0, 1, new Deck());
//        round = new Round(state);
//        assertEquals(0, round.getBlindScore());
//        assertEquals(1, round.getRemainingPlays());
//        assertEquals(0, round.getCurrentScore());
//        assertEquals(3, round.getRemainingDiscards());
//    }
//
//    @Test
//    public void round_incorrectInitializatioin() throws JavatroException {
//        // test zero plays
//        State state = new State(100, 0, new Deck());
//        try {
//            new Round(state);
//            fail();
//        } catch (JavatroException e) {
//            assertEquals("Number of plays per round must be greater than 0", e.getMessage());
//        }
//
//        // test negative blind score
//        state = new State(-100, 3, new Deck());
//        try {
//            new Round(state);
//            fail();
//        } catch (JavatroException e) {
//            assertEquals("Blind score must be greater than or equal to 0", e.getMessage());
//        }
//
//        // test null deck
//        state = new State(100, 3, null);
//        try {
//            new Round(state);
//            fail();
//        } catch (JavatroException e) {
//            assertEquals("Deck cannot be null", e.getMessage());
//        }
//
//        // test negative blind score and zero plays
//        state = new State(-100, 0, new Deck());
//        try {
//            new Round(state);
//            fail();
//        } catch (JavatroException e) {
//            assertEquals("Blind score must be greater than or equal to 0", e.getMessage());
//        }
//
//        // test negative blind score and null deck
//        state = new State(-100, 3, null);
//        try {
//            new Round(state);
//            fail();
//        } catch (JavatroException e) {
//            assertEquals("Blind score must be greater than or equal to 0", e.getMessage());
//        }
//
//        // test zero plays and null deck
//        state = new State(100, 0, null);
//        try {
//            new Round(state);
//            fail();
//        } catch (JavatroException e) {
//            assertEquals("Number of plays per round must be greater than 0", e.getMessage());
//        }
//
//        // test negative blind score, zero plays and null deck
//        state = new State(-100, 0, null);
//        try {
//            new Round(state);
//            fail();
//        } catch (JavatroException e) {
//            assertEquals("Blind score must be greater than or equal to 0", e.getMessage());
//        }
//    }
// }
