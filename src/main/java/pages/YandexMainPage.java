package pages;

import core.BasePage;
import helpers.PageHelper;
import helpers.Properties;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Класс главной страницы Яндекс.
 * @author Кирилл Желтышев
 */
public class YandexMainPage extends BasePage {
    /**
     * Локатор строки поиска.
     * @author Кирилл Желтышев
     */
    private final String searchFieldLocator = "//*[@id='text']";
    /**
     * Локатор кнопки "все"
     * @author Кирилл Желтышев
     */
    private final String moreServicesButtonLocator = "//*[@class='services-suggest__icons-more']";
    /**
     * Локатор кнопки "Маркет" в сервисах.
     * @author Кирилл Желтышев
     */
    private final String popupMarketButtonLocator = "//*[@data-id='market']/ancestor::span";

    /**
     * Конструктор класса.
     * @author Кирилл Желтышев
     * @param webDriver Web-драйвер.
     */
    public YandexMainPage(WebDriver webDriver) {
        super(webDriver);
    }

    /**
     * Метод открытия главной страницы Яндекса.
     * @author Кирилл Желтышев
     */
    @Step("Открываем главную страницу.")
    public void open() {
        driver.get(Properties.testProperties.yandexUrl());
    }

    /**
     * Метод открытия окна сервисов на главной странице.
     * @author Кирилл Желтышев
     */
    @Step("Открываем \"Все сервисы\".")
    public void openScrollbarServices() {
        WebElement searchField = driver.findElement(By.xpath(searchFieldLocator));
        searchField.click();
        WebElement moreServicesButton = driver.findElement(By.xpath(moreServicesButtonLocator));
        moreServicesButton.click();
    }

    /**
     * Метод открытия и перехода страницы Яндекс Маркета.
     * @author Кирилл Желтышев
     */
    @Step("Открываем Яндекс Маркет.")
    public void openMarket() {
        WebElement popupMarketButton = driver.findElement(By.xpath(popupMarketButtonLocator));
        popupMarketButton.click();
        PageHelper.switchToLastTab(driver);
    }
}
