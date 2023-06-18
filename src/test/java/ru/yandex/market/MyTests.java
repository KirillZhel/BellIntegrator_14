package ru.yandex.market;

import com.browserup.harreader.model.Har;
import com.browserup.harreader.model.HarEntry;
import com.jayway.jsonpath.JsonPath;
import core.*;
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
    public void MyTest() {
        List<String> manufacturersForFilter = List.of("Lenovo", "HUAWEI");
        List<String> manufacturersLowerCase = manufacturersForFilter.stream().map(String::toLowerCase).collect(Collectors.toList());
        int minPriceForFilter = 10000;
        int maxPriceForFilter = 900000;
        int snippetsNumber = 12;

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
        yandexMarketSERPFactory.priceFilter.setMinPrice(minPriceForFilter);
        yandexMarketSERPFactory.priceFilter.setMaxPrice(maxPriceForFilter);

        List<Snippet> snippets = getAllSnippetFromPage(yandexMarketSERPFactory);
        Assertions.assertTrue(snippets.size() > snippetsNumber, "На странице отображается меньше " + snippetsNumber + "элементов.");

        Assertions.assertTrue(
                areAllSnippetOnPageMatchTheFilter(snippets, manufacturersLowerCase, minPriceForFilter, maxPriceForFilter),
                "Не все предложения на 1 странице соответствуют фильтру");

        SearchResultsInfo info = getSearchResultsInfo();

        String baseUrl = driver.getCurrentUrl();
        int randomPage = goToRandomPage(info);
        snippets = getAllSnippetFromPage(yandexMarketSERPFactory);
        Assertions.assertTrue(areAllSnippetOnPageMatchTheFilter(snippets, manufacturersLowerCase, minPriceForFilter, maxPriceForFilter),
                "Не все предложения на странице " + randomPage + " соответствуют фильтру");

        int lastPage = goToLastPage(info);
        snippets = getAllSnippetFromPage(yandexMarketSERPFactory);
        Assertions.assertTrue(areAllSnippetOnPageMatchTheFilter(snippets, manufacturersLowerCase, minPriceForFilter, maxPriceForFilter),
                "Не все предложения на странице " + lastPage + " соответствуют фильтру");

        driver.get(baseUrl);
        Snippet firstSnippet = getAllSnippetFromPage(yandexMarketSERPFactory).stream().findFirst().get();
    }

    private int goToLastPage(SearchResultsInfo info) {
        int lastPageNumber = getLastPageNumber(info);
        goToPage(lastPageNumber);
        return lastPageNumber;
    }

    private int goToRandomPage(SearchResultsInfo info) {
        int randomPageNumber = getRandomPageNumber(info);
        goToPage(randomPageNumber);
        return randomPageNumber;
    }

    private void goToPage(int pageNumber) {
        String randomPageUrl = UriUtils.addQueryParameters(driver.getCurrentUrl(), Map.of("page", String.valueOf(pageNumber)));
        driver.get(randomPageUrl);
    }

    public int getRandomPageNumber(SearchResultsInfo info) {
        int lastPageNumber = getLastPageNumber(info);
        int firstPageNumber = 1;
        return RandomUtils.nextInt(firstPageNumber + 1, lastPageNumber);
    }

    public int getLastPageNumber(SearchResultsInfo info) {
        return Math.min(info.total, 30);
    }

    private boolean areAllSnippetOnPageMatchTheFilter(List<Snippet> snippets, List<String> manufacturersLowerCase, int minPriceForFilter, int maxPriceForFilter) {
        return areAllSnippetsContainsAnyStringInName(snippets, manufacturersLowerCase)
                && areAllSnippetsHaveCurrentPrice(snippets, minPriceForFilter, maxPriceForFilter);
    }


    private List<Snippet> getAllSnippetFromPage(YandexMarketSERPFactory yandexMarketSERPFactory)
    {
        yandexMarketSERPFactory.waitForDataLoaded();
        yandexMarketSERPFactory.scrollAllSnippets();
        return yandexMarketSERPFactory.getShownSnippetsList();
    }

    private SearchResultsInfo getSearchResultsInfo()
    {
        waitForState(
                () -> getMostRecentHarEntryForSearchRequest(proxy.getHar()),
                harEntry -> harEntry
                        .getResponse()
                        .getContent()
                        .getSize() > 0);

        return extractLatestSearchResultsResponse(proxy.endHar());
    }

    private boolean areAllSnippetsHaveCurrentPrice(List<Snippet> snippets, int minPrice, int maxPrice)
    {
        return snippets.stream().allMatch(snippet -> snippet.price >= minPrice && snippet.price <= maxPrice);
    }

    private boolean areAllSnippetsContainsAnyStringInName(List<Snippet> snippets, List<String> strings) {
        return snippets.stream()
                .map(snippet -> snippet.name.toLowerCase())
                .allMatch(snippetName -> strings.stream()
                        .anyMatch(snippetName::contains));
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
}
