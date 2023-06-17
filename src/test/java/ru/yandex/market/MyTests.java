package ru.yandex.market;

import com.browserup.harreader.model.Har;
import com.browserup.harreader.model.HarEntry;
import com.jayway.jsonpath.JsonPath;
import core.BaseTest;
import core.Helper;
import core.SearchResultsInfo;
import core.UriUtils;
import io.qameta.allure.Feature;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.YandexMainPage;
import pages.YandexMarketMainPage;
import pages.YandexMarketSERPFactory;
import pages.elements.Snippet;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static core.WaitUtils.*;

public class MyTests extends BaseTest {

    @BeforeEach
    public void before() {
        proxy.newHar();
    }

    @Feature("Yandex Market")
    @DisplayName("Проверка выдачи товаров")
    @Test
    public void MyTest() throws InterruptedException {
        List<String> manufacturersForFilter = List.of("Lenovo", "HUAWEI");
        List<String> manufacturersLowerCase = manufacturersForFilter.stream().map(String::toLowerCase).collect(Collectors.toList());
        int minPriceForFilter = 10000;
        int maxPriceForFilter = 900000;
        List<Integer> pagesToCheck = List.of(1,5,10);


        YandexMainPage yandexMainPage = new YandexMainPage(driver);
        yandexMainPage.open();
        yandexMainPage.popupMarketButton.click();
        Helper.switchToLastTab(driver);

        YandexMarketMainPage yandexMarketMainPage = new YandexMarketMainPage(driver);
        yandexMarketMainPage.clickToCatalogButton();
        yandexMarketMainPage.hoverToCategory("Ноутбуки и компьютеры");
        yandexMarketMainPage.clickToSubcategory("Ноутбуки");

        YandexMarketSERPFactory yandexMarketSERPFactory = new YandexMarketSERPFactory(driver);
        yandexMarketSERPFactory.manufacturerFilter.clickShowAllButton();
        yandexMarketSERPFactory.manufacturerFilter.chooseManufacturers(manufacturersForFilter);
        yandexMarketSERPFactory.priceFilter.setMinPrice(10000);
        yandexMarketSERPFactory.priceFilter.setMaxPrice(900000);

        yandexMarketSERPFactory.waitForDataLoaded();

        String anotherUrl = UriUtils.addQueryParameters(driver.getCurrentUrl(), Map.of("page", "4"));
        driver.get(anotherUrl);

        //проверить страницу
        //перейти на рандом.сттаницу
        //проверить
        //перейти на последнюю
        //проверить
        //перейти на первую
        //
        //поиск первого товара
        //String firstProductName = yandexMarketSERPFactory.getProductNameByIndex(1);
        // data-auto="spinner" - может проверить, если данный элемент на странице. Если нет, то уже брать список товаров
        //List<WebElement> spinner = driver.findElements(By.xpath("//*[@data-auto='spinner']"));
        //проверка страницы на соответствие фильтрам
        //yandexMarketSERPFactory.scroolToFooter();
        yandexMarketSERPFactory.scrollAllSnippets();

        List<Snippet> snippets = yandexMarketSERPFactory.getShownSnippetsList();
        boolean allSnippetsHaveFilteredManufacturerInName = this.areAllSnippetsContainsAnyStringInName(snippets, manufacturersLowerCase);
        boolean allSnippetsHaveFilteredPrice = snippets.stream()
                .allMatch(snippet -> snippet.price >= minPriceForFilter && snippet.price <= maxPriceForFilter);


        List<Snippet> allSnippets = yandexMarketSERPFactory.snippets;

        boolean ansList = yandexMarketSERPFactory.checker.checkManufacturer(
                yandexMarketSERPFactory.manufacturerFilter.getManufacturerList(),
                allSnippets);
        boolean ansMap = yandexMarketSERPFactory.checker.checkManufacturer(
                yandexMarketSERPFactory.manufacturerFilter.getManufacturerList(),
                yandexMarketSERPFactory.map);

        waitForState(
                () -> getMostRecentHarEntryForSearchRequest(proxy.getHar()),
                harEntry -> harEntry
                        .getResponse()
                        .getContent()
                        .getSize() > 0);

        SearchResultsInfo info = extractLatestSearchResultsResponse(proxy.endHar());
        int lastPageNumber = Math.min(info.total, 30);
        int firstPageNumber = 1;
        int randomPageNumber = RandomUtils.nextInt(firstPageNumber + 1, lastPageNumber);

    }

    private SearchResultsInfo extractLatestSearchResultsResponse(Har har) {
        HarEntry mostRecentEntry = getMostRecentHarEntryForSearchRequest(har);
        String rawJson = mostRecentEntry.getResponse().getContent().getText();

        Map<String, Integer> rawData = (Map<String, Integer>) JsonPath
                .parse(rawJson)
                .read("$.results..visibleSearchResult.*['total', 'itemsPerPage', 'page']", List.class)
                .get(0);


        return new SearchResultsInfo(
                rawData.get("total"),
                rawData.get("itemsPerPage"),
                rawData.get("page")
        );

    }

    private HarEntry getMostRecentHarEntryForSearchRequest(Har har) {
        Optional<HarEntry> mostRecentEntry = har.getLog().findMostRecentEntry(Pattern.compile(".*search/resolveRemoteSearch.*"));
        return mostRecentEntry
                .orElseThrow(() -> new RuntimeException("Wasn't able to find response for market search"));
    }

    /*
    1. Сделать класс проверяльщик (статический?)
    2. В его метод вкидываются цены и производители и список для проверки
    3. В нем есть методы для проверки цены, производителя по отдельности, возвращают булы
    4. Общий метод, который вызывает эти методы, возвращает булы
    */

    /*
    1. Метод, который возвращает ссылки на страницы, которые надо проверить (страницы магазина).
    Поместить тоже в класс статический. В него помещаются базовая ссылка и класс, который содержит
    информацию о количестве товаров и страниц. В классе есть рандомайзер для создания ссылок на
    промежуточные страницы
    */
    private boolean areAllSnippetsContainsAnyStringInName(List<Snippet> snippets, List<String> strings) {
        return snippets.stream()
                .map(snippet -> snippet.name.toLowerCase())
                .allMatch(snippetName -> strings.stream()
                        .anyMatch(snippetName::contains));
    }
}
