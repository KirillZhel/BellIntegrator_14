package core;

import org.awaitility.Awaitility;

import java.time.Duration;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class WaitUtils {

    public static <T> void waitForState(Supplier<T> objSupplier, Predicate<T> predicate) {
        Awaitility
                .await()
                .ignoreExceptions()
                .atMost(Duration.ofSeconds(20))
                .pollInterval(Duration.ofSeconds(1))
                .until(() -> predicate.test(objSupplier.get()));
    }
}
