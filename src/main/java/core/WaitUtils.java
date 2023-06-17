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

    public static <T> void waitFor(boolean condition) {
        Awaitility
                .await()
                .ignoreExceptions()
                .atMost(Duration.ofSeconds(20))
                .pollInterval(Duration.ofSeconds(1))
                .until(() -> condition);
    }

    public static void wait(Duration duration) {
        try {
            Thread.sleep(duration.toMillis());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
