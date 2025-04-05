package javatro.core.jokers;

// @@author jwyk

import javatro.core.jokers.addchip.OddToddJoker;
import javatro.core.jokers.addchip.ScaryFaceJoker;
import javatro.core.jokers.addmult.*;
import javatro.core.jokers.addmult.AbstractJoker;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

/** Factory for creating and distributing Jokers. */
public class JokerFactory {
    private static final List<Supplier<Joker>> jokerSuppliers =
            List.of(
                    OddToddJoker::new,
                    ScaryFaceJoker::new,
                    AbstractJoker::new,
                    GluttonousJoker::new,
                    GreedyJoker::new,
                    HalfJoker::new,
                    LustyJoker::new,
                    WrathfulJoker::new);

    private static final Random random = new Random();

    /**
     * Returns a random Joker from the above list.
     *
     * @return A Joker from the List of Jokers above.
     */
    public static Joker createRandomJoker() {
        return jokerSuppliers.get(random.nextInt(jokerSuppliers.size())).get();
    }
}
