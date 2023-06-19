package helpers;

import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

/**
 * Класс, поставщик данных.
 * @author Кирилл Желтышев
 */
public class DataProvider {
    /**
     * Метод, поставляющий тестовые аргументы для проверки Яндекс Маркета.
     * @author Кирилл Желтышев
     * @return Поток аргументов теста
     */
    public static Stream<Arguments> provideCheckingMarket() {
        String category = "Ноутбуки и компьютеры";
        String subcategory = "Ноутбуки";
        List<String> manufacturersForFilter = List.of("Lenovo", "HUAWEI");
        int minPriceForFilter = 10000;
        int maxPriceForFilter = 900000;
        int minSnippetsNumber = 12;

        return Stream.of(
                Arguments.of(
                        category,
                        subcategory,
                        manufacturersForFilter,
                        minPriceForFilter,
                        maxPriceForFilter,
                        minSnippetsNumber));
    }
}