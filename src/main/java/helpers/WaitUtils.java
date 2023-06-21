package helpers;

import org.awaitility.Awaitility;

import java.time.Duration;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Класс, содержпщий дополнительные методы работы ожиданиями
 * @author Кирилл Желтышев
 */
public class WaitUtils {

    /**
     * Метод ожидания состояния
     * @author Кирилл Желтышев
     * @param objSupplier объект ожидания
     * @param predicate условие ожидания
     * @param <T> тип возвращаемого объекта
     */
    public static <T> void waitForState(Supplier<T> objSupplier, Predicate<T> predicate) {
        Awaitility
                .await()
                .ignoreExceptions()
                .atMost(Duration.ofSeconds(20))
                .pollInterval(Duration.ofSeconds(1))
                .until(() -> predicate.test(objSupplier.get()));
    }

    /**
     * Метод ожидания по времени
     * @author Кирилл Желтышев
     * @param duration время ожидания
     */
    public static void wait(Duration duration) {
        Awaitility.await().pollDelay(duration).until(() -> true);
    }
}
