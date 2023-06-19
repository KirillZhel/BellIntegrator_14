package helpers;

import org.aeonbits.owner.Config;

/**
 * Интерфейс, для класса, содержащего глобальные свойства из tests.properties.
 * @author Кирилл Желтышев
 */
@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({"file:src/test/resources/tests.properties"})
public interface TestProperties extends Config {
    /**
     * Сигнатура метода, возвращающего url главной страници Яндекса.
     * @author Кирилл Желтышев
     */
    @Config.Key("yandex.url")
    String yandexUrl();
}
