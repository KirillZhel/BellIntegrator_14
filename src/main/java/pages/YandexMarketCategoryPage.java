package pages;

import core.BasePage;
import helpers.UriUtils;
import helpers.WaitUtils;
import io.qameta.allure.Step;
import models.SearchResultsInfo;
import org.apache.commons.lang3.RandomUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import pages.elements.ManufacturerFilter;
import pages.elements.PriceFilter;
import models.Snippet;

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
    private final PriceFilter priceFilter;
    /**
     * Фильтр производителей.
     * @author Кирилл Желтышев
     */
    private final ManufacturerFilter manufacturerFilter;

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
     * Геттер для фильтра по цене.
     * @author Кирилл Желтышев
     * @return Элемент фильтра по цене
     */
    public PriceFilter getPriceFilter() {
        return priceFilter;
    }

    /**
     * Геттер для фильтра по производителям.
     * @author Кирилл Желтышев
     * @return Элемент фильтра по производителям
     */
    public ManufacturerFilter getManufacturerFilter() {
        return manufacturerFilter;
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
    @Step("Собираем все карточки, которые загружены на странице.")
    private List<Snippet> getShownSnippetsList() {
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
    @Step("Ожидаем принятия фильтра и прогрузки товаров.")
    public void waitForDataLoaded() {
        Supplier<WebElement> spinner = () -> driver.findElement(By.xpath(spinnerLocator));
        WaitUtils.waitForState(spinner, it -> !it.isDisplayed());
        WaitUtils.waitForState(() -> driver.findElements(By.xpath(productSnippetLocator)),
                elements -> elements.size() > 0);
    }

    /**
     * Метод прокрутки страницы, пока подгружаются новые товары.
     * @author Кирилл Желтышев
     */
    @Step("Скролим до тех пор, пока на странице подгружаются товары.")
    private void scrollAllSnippets() {
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

    /**
     * Метод перехода на последнюю страницу выдачи товаров.
     * @author Кирилл Желтышев
     * @param info Объект дополнительной информации поиска
     * @return Номер последней страницы
     */
    @Step("Переходим на последнюю страницу.")
    public int goToLastPage(SearchResultsInfo info) {
        int lastPageNumber = getLastPageNumber(info);
        goToPage(lastPageNumber);
        return lastPageNumber;
    }

    /**
     * Метод перехода на случайную страницу выдачи товаров.
     * @author Кирилл Желтышев
     * @param info Объект дополнительной информации поиска
     * @return Номер случайной страницы
     */
    @Step("Переходим на случайную страницу.")
    public int goToRandomPage(SearchResultsInfo info) {
        int randomPageNumber = getRandomPageNumber(info);
        goToPage(randomPageNumber);
        return randomPageNumber;
    }

    /**
     * Метод перехода на первую страницу выдачи товаров.
     * @author Кирилл Желтышев
     * @return Номер первой страницы
     */
    @Step("Переходим на первую страницу.")
    public int goToFirstPage() {
        goToPage(1);
        return 1;
    }

    /**
     * Метод перехода на страницу выдачи товаров.
     * @author Кирилл Желтышев
     * @param pageNumber Номер страницы
     */
    @Step("Переходим на {pageNumber} страницу.")
    public void goToPage(int pageNumber) {
        String randomPageUrl = UriUtils.addQueryParameters(driver.getCurrentUrl(), Map.of("page", String.valueOf(pageNumber)));
        driver.get(randomPageUrl);
    }

    /**
     * Метод, возвращающий случайный номер страницы.
     * @author Кирилл Желтышев
     * @param info Объект дополнительной информации поиска
     * @return Случайный номер страницы
     */
    private int getRandomPageNumber(SearchResultsInfo info) {
        int lastPageNumber = getLastPageNumber(info);
        int firstPageNumber = 1;
        return RandomUtils.nextInt(firstPageNumber + 1, lastPageNumber);
    }

    /**
     * Метод, возвращающий номер последней страницы выдачи.
     * @author Кирилл Желтышев
     * @param info Объект дополнительной информации поиска
     * @return Номер последней страницы
     */
    private int getLastPageNumber(SearchResultsInfo info) {
        return Math.min(info.pagesCount, 30);
    }
}
