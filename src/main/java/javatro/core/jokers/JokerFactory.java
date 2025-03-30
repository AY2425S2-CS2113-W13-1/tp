package javatro.core.jokers;

import javatro.core.jokers.addchip.OddToddJoker;
import javatro.core.jokers.addchip.ScaryFaceJoker;
import javatro.core.jokers.addmult.CounterJoker;
import javatro.core.jokers.addmult.GluttonousJoker;
import javatro.core.jokers.addmult.GreedyJoker;
import javatro.core.jokers.addmult.HalfJoker;
import javatro.core.jokers.addmult.LustyJoker;
import javatro.core.jokers.addmult.WrathfulJoker;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

/** Factory for creating and distributing Jokers. */
public class JokerFactory {
    private static final List<Supplier<Joker>> jokerSuppliers =
            List.of(
                    OddToddJoker::new,
                    ScaryFaceJoker::new,
                    CounterJoker::new,
                    GluttonousJoker::new,
                    GreedyJoker::new,
                    HalfJoker::new,
                    LustyJoker::new,
                    WrathfulJoker::new);

    private static final Random random = new Random();

    /**
     * Returns a random Joker from the above list.
     *
     * @return A RandomJoker
     */
    public static Joker createRandomJoker() {
        return jokerSuppliers.get(random.nextInt(jokerSuppliers.size())).get();
    }
}
