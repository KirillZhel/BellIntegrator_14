package core;

import io.qameta.allure.Step;

/**
 * Класс содержит методы-обёртки для стандартных методов Assertions библиотеки jupiter, чтобы проверки попали в allure-отчёт.
 * @author Кирилл Желтышев
 */
public class Assertions {
    /**
     * Метод-обёртка над методом assertTrue.
     * @author Кирилл Желтышев
     * @param condition выражение для проверки
     * @param message сообщение в случае провала проверки
     */
    @Step("Проверяем что нет ошибки: {message}")
    public static void assertTrue(boolean condition, String message) {
        org.junit.jupiter.api.Assertions.assertTrue(condition, message);
    }
}
