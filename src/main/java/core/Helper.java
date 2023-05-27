package core;

import org.openqa.selenium.WebDriver;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Класс, содержащий вспомогательные методы.
 * @author Кирилл Желтышев
 */
public class Helper {
    /**
     * Метод парсинга строки в double, если строка содержит запятую в качестве разделителя.
     * @author Кирилл Желтышев
     * @param str строка для парсинга
     * @return double после парсинга строки
     * @throws ParseException исключение, если не удалось спарсить строку
     */
    public static double parse(String str) throws ParseException {
        NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
        Number number = format.parse(str);
        return number.doubleValue();
    }

    /**
     * Метод переключения на последнюю вкладку браузера, которую открыл web-драйвер.
     * @author Кирилл Желтышев
     * @param driver web-драйвер для которого надо переключить вкладку
     */
    public static void switchToLastTab(WebDriver driver) {
        List<String> handles = new ArrayList<>(driver.getWindowHandles());
        String lastTab = handles.get(handles.size() - 1);
        driver.switchTo().window(lastTab);
    }
}
