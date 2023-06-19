package core;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.interactions.Actions;

import java.time.Duration;

/**
 * Базовый класс страниц.
 * @author Кирилл Желтышев
 */
public class BasePage {
    /**
     * Web-драйвер.
     * @author Кирилл Желтышев
     */
    protected WebDriver driver;
    /**
     * Объект явного ожидания.
     * @author Кирилл Желтышев
     */
    protected WebDriverWait wait;
    /**
     * Объект для для эмуляции сложных пользовательских жестов.
     * @author Кирилл Желтышев
     */
    protected Actions actions;

    /**
     * Конструктор базовой страницы.
     * @param webDriver web-драйвер.
     */
    public BasePage(WebDriver webDriver) {
        driver = webDriver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        actions = new Actions(driver);
    }
}
