package pages.elements;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.List;

/**
 * Класс фильтра по производителям.
 * @author Кирилл Желтышев
 */
public class ManufacturerFilter {
    /**
     * Локатор фильтра производителей.
     * @author Кирилл Желтышев
     */
    private final String manufacturerFilterLocator = "//*[@data-baobab-name='filter'][.//legend[.='Производитель']]";
    /**
     * Локатор конпки "Показать всё" фильтра производителей.
     * @author Кирилл Желтышев
     */
    private final String showAllButtonLocator = ".//*[@data-zone-name='LoadFilterValues']";
    /**
     * Локатор строки ввода фильтра производителей.
     * @author Кирилл Желтышев
     */
    private final String filterInputLocator = ".//input[./preceding-sibling::label[text()='Найти производителя']]";
    /**
     * Формализованная строка для локатора чек-боксов в фильтре производителей.
     * @author Кирилл Желтышев
     */
    private final String optionLabelLocatorFormatString = ".//*[@data-baobab-name='filterValue'][(.)='%s']//label";
    /**
     * Web-драйвер.
     * @author Кирилл Желтышев
     */
    private final WebDriver driver;

    /**
     * Конструктор класса.
     * @author Кирилл Желтышев
     */
    public ManufacturerFilter(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Метод раскрытия полного списка производителей по нажатию на кнопку "Показать всё".
     * @author Кирилл Желтышев
     */
    @Step("Нажимаем на кнопку \"Показать всё\" у фильтра \"Производитель\"")
    public void clickShowAllButton() {
        WebElement button = driver
                .findElement(By.xpath(manufacturerFilterLocator))
                .findElement(By.xpath(showAllButtonLocator));

        button.click();
    }

    /**
     * Метод выбора списка производителей в фильтре.
     * @author Кирилл Желтышев
     */
    @Step("Выбираем производителей")
    public void chooseManufacturers(List<String> manufacturers) {
        manufacturers.forEach(this::chooseManufacturer);
    }

    /**
     * Метод выбора производителя в фильтре.
     * @author Кирилл Желтышев
     */
    @Step("Выбираем производителя: \"{name}\"")
    private void chooseManufacturer(String name) {
        clearFilterInput();
        typeTextToInput(name);
        clickOption(name);
    }

    /**
     * Метод, печатающий в строку ввода наименования производителя в фильтре.
     * @author Кирилл Желтышев
     */
    private void typeTextToInput(String text) {
        filterInput().sendKeys(text);
    }

    /**
     * Клик по чекбоксу у найденого производителя.
     * @author Кирилл Желтышев
     * @param optionName имя производителя, на чек-бокс которого надо кликнуть
     */
    private void clickOption(String optionName) {
        WebElement option = filterContainer().findElement(By.xpath(filterOptionLabel(optionName)));
        option.click();
    }
    /**
     * Очистка строки ввода.
     * @author Кирилл Желтышев
     */
    private void clearFilterInput() {
        filterInput().clear();
    }

    /**
     * Метод поиска на странице строки поиска в фильтре производителей.
     * @return web-элемент строки ввода
     */
    private WebElement filterInput() {
        return filterContainer()
                .findElement(By.xpath(filterInputLocator));
    }

    /**
     * Метод составления локатора чек-бокса производителя.
     * @param optionName имя производителя, для которого составляется локатор
     * @return локатор чек-бокса производителя.
     */
    private String filterOptionLabel(String optionName) {
        return String.format(optionLabelLocatorFormatString, optionName);
    }

    /**
     * Метод поиска на странице фильтра.
     * @return web-элемент фильтра
     */
    private WebElement filterContainer() {
        return driver.findElement(By.xpath(manufacturerFilterLocator));
    }
}
