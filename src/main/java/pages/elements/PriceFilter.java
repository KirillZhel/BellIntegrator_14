package pages.elements;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Класс фильтра по цене.
 * @author Кирилл Желтышев
 */
public class PriceFilter {
    /**
     * Локатор строки ввода минимальной цены.
     * @author Кирилл Желтышев
     */
    private final String minPriceInputLocator = "//*[@data-filter-id='glprice']//*[@data-auto='filter-range-min']//input";
    /**
     * Локатор строки ввода максимальной цены.
     * @author Кирилл Желтышев
     */
    private final String maxPriceInputLocator = "//*[@data-filter-id='glprice']//*[@data-auto='filter-range-max']//input";
    /**
     * Web-драйвер.
     * @author Кирилл Желтышев
     */
    private final WebDriver driver;

    /**
     * Конструктор класса.
     * @author Кирилл Желтышев
     */
    public PriceFilter(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Метод установки минимальной цены в фильтре.
     * @author Кирилл Желтышев
     * @param minPrice вводимая минимальная цена
     */
    @Step("Устанавливаем минимальную цену: \"{minPrice}\".")
    public void setMinPrice(int minPrice) {
        filterInput(minPriceInputLocator, minPrice);
    }

    /**
     * Метод установки максимальной цены в фильтре.
     * @author Кирилл Желтышев
     * @param maxPrice вводимая максимальная цена
     */
    @Step("Устанавливаем максимальную цену: \"{maxPrice}\".")
    public void setMaxPrice(int maxPrice) {
        filterInput(maxPriceInputLocator, maxPrice);
    }

    /**
     * Метод установки в фильтр цены.
     * @author Кирилл Желтышев
     * @param locator локатор строки ввода фильтра.
     * @param price вводимая цена
     */
    @Step("Печатаем в web-элемент (локатор: \"{locator}\") цену {price}.")
    private void filterInput(String locator, int price) {
        WebElement input =  driver.findElement(By.xpath(locator));
        input.clear();
        input.sendKeys(Integer.toString(price));
    }
}
