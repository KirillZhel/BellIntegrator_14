package pages;

import core.BasePage;
import helpers.WaitUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import pages.elements.ManufacturerFilter;
import pages.elements.PriceFilter;
import pages.elements.Snippet;

import java.time.Duration;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Класс страницы категории
 * @author Кирилл Желтышев
 */
public class YandexMarketCategoryPage extends BasePage {
    /**
     * Локатор карточки товара.
     * @author Кирилл Желтышев
     */
    private final String productSnippetLocator = "//*[@data-autotest-id='product-snippet']";
    /**
     * Локатор спинера(появляется при загрузке).
     * @author Кирилл Желтышев
     */
    private final String spinnerLocator = "//*[@data-auto='spinner']";
    /**
     * Локатор наименования товара в карточке.
     * @author Кирилл Желтышев
     */
    private final String snippetTitleLocator = ".//*[@data-auto='snippet-title-header']";
    /**
     * Локатор цены товара в карточке.
     * @author Кирилл Желтышев
     */
    private final String snippetPriceLocator = ".//*[@data-auto='price-value']";
    /**
     * Локатор строки поиска.
     * @author Кирилл Желтышев
     */
    private final String searchFieldLocator = "//*[@id='header-search']";

    /**
     * Ценовой фильтр.
     * @author Кирилл Желтышев
     */
    public PriceFilter priceFilter;
    /**
     * Фильтр производителей.
     * @author Кирилл Желтышев
     */
    public ManufacturerFilter manufacturerFilter;

    /**
     * Конструктор класса.
     * @author Кирилл Желтышев
     * @param webDriver Web-драйвер
     */
    public YandexMarketCategoryPage(WebDriver webDriver) {
        super(webDriver);
        priceFilter = new PriceFilter(webDriver);
        manufacturerFilter = new ManufacturerFilter(webDriver);
    }

    /**
     * Метод, который ожидает прогрузки всех карточек товаров и собирает их в список.
     * @author Кирилл Желтышев
     * @return Список всех карточек товаров на странице
     */
    @Step("Собираем все карточки предложенных товаров.")
    public List<Snippet> getAllSnippetFromPage() {
        waitForDataLoaded();
        scrollAllSnippets();
        return getShownSnippetsList();
    }

    /**
     * Метод поиска через строку поиска товара.
     * @author Кирилл Желтышев
     * @param query Запрос для поиска
     */
    @Step("Ищем в поиске Яндекс Маркет: \"{query}\".")
    public void search(String query) {
        WebElement searchField = driver.findElement(By.xpath(searchFieldLocator));
        searchField.sendKeys(query);
        searchField.submit();
    }

    /**
     * Метод, собирающий все карточки товаров на странице.
     * @author Кирилл Желтышев
     * @return Список всех карточек товаров на странице
     */
    public List<Snippet> getShownSnippetsList() {
        List<WebElement> elements = driver.findElements(By.xpath(productSnippetLocator));
        return elements.stream().map(e -> {
            String name = e.findElement(By.xpath(snippetTitleLocator)).getText();
            String rawPrice = e.findElement(By.xpath(snippetPriceLocator)).getText().replaceAll("\\D*", "");
            int price = Integer.parseInt(rawPrice);
            return new Snippet(name, price);
        })
                .collect(Collectors.toList());
    }

    /**
     * Метод, ожидающий прогрузки карточек товаров.
     * @author Кирилл Желтышев
     */
    public void waitForDataLoaded() {
        Supplier<WebElement> spinner = () -> driver.findElement(By.xpath(spinnerLocator));
        WaitUtils.waitForState(spinner, it -> !it.isDisplayed());
    }

    /**
     * Метод прокрутки страницы, пока подгружаются новые товары.
     * @author Кирилл Желтышев
     */
    public void scrollAllSnippets() {
        List<WebElement> previousSnippetsList = this.driver.findElements(By.xpath(productSnippetLocator));

        while(true) {
            WebElement lastSnippet = previousSnippetsList.get(previousSnippetsList.size() - 1);
            actions
                    .scrollToElement(lastSnippet)
                    .scrollByAmount(0, lastSnippet.getRect().height / 2)
                    .perform();
            WaitUtils.wait(Duration.ofMillis(500));
            List<WebElement> currentSnippetsList = this.driver.findElements(By.xpath(productSnippetLocator));
            if (previousSnippetsList.size() == currentSnippetsList.size()) return;
            previousSnippetsList = currentSnippetsList;
        }
    }
}
