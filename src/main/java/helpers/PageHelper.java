package helpers;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс, содержащий вспомогательные методы.
 * @author Кирилл Желтышев
 */
public class PageHelper {
    /**
     * Метод переключения на последнюю вкладку браузера, которую открыл web-драйвер.
     * @author Кирилл Желтышев
     * @param driver web-драйвер для которого надо переключить вкладку
     */
    @Step("Переходим на последнюю открывшуюся страницу в окне браузера.")
    public static void switchToLastTab(WebDriver driver) {
        List<String> handles = new ArrayList<>(driver.getWindowHandles());
        String lastTab = handles.get(handles.size() - 1);
        driver.switchTo().window(lastTab);
    }
}
