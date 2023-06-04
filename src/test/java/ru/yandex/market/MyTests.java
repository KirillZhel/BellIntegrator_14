package ru.yandex.market;

import com.browserup.harreader.model.Har;
import com.browserup.harreader.model.HarEntry;
import com.jayway.jsonpath.JsonPath;
import core.BaseTest;
import core.Helper;
import core.MarketVisibleSearchResults;
import core.WaitUtils;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.YandexMainPage;
import pages.YandexMarketMainPage;
import pages.YandexMarketSERPFactory;

import java.util.*;
import java.util.regex.Pattern;

public class MyTests extends BaseTest {

    @BeforeEach
    public void before() {
        proxy.newHar();
    }

    @Feature("Yandex Market")
    @DisplayName("Проверка выдачи товаров")
    @Test
    public void MyTest() throws InterruptedException {
        YandexMainPage yandexMainPage = new YandexMainPage(driver);
        yandexMainPage.open();
        yandexMainPage.popupMarketButton.click();
        Helper.switchToLastTab(driver);

        YandexMarketMainPage yandexMarketMainPage = new YandexMarketMainPage(driver);
        yandexMarketMainPage.createCategoryLocatorString("Ноутбуки и компьютеры");
        yandexMarketMainPage.createSubcategoryLocatorString("Ноутбуки");
        yandexMarketMainPage.getAllElements();
        yandexMarketMainPage.subcategory.click();

        YandexMarketSERPFactory yandexMarketSERPFactory = new YandexMarketSERPFactory(driver);
        yandexMarketSERPFactory.manufacturerFilter2.clickShowAllButton();
        yandexMarketSERPFactory.manufacturerFilter2.chooseManufacturer("Lenovo");
        yandexMarketSERPFactory.manufacturerFilter2.chooseManufacturer("HUAWEI");

        // надо придумать гибкое ожидание ответа (можно дождаться, пока спиннер загрузки списка товаров пропадет или мб у селениума есть встроенные методы)
        // ну или просто в цикле брать список записанных ответов и проверять, пока не появится ответ с телом для запроса поиска

        WaitUtils.waitForState(
                () -> getMostRecentHarEntryForSearchRequest(proxy.getHar()),
                harEntry -> harEntry
                        .getResponse()
                        .getContent()
                        .getSize() > 0);

        // тут надо дождаться, пока запрос состоится
        MarketVisibleSearchResults resp = extractLatestSearchResultsResponse(proxy.endHar());
    }

    private MarketVisibleSearchResults extractLatestSearchResultsResponse(Har har) {
        HarEntry mostRecentEntry = getMostRecentHarEntryForSearchRequest(har);
        String rawJson = mostRecentEntry.getResponse().getContent().getText();

        Map<String, Integer> rawData = (Map<String, Integer>) JsonPath
                .parse(rawJson)
                .read("$.results..visibleSearchResult.*['total', 'itemsPerPage', 'page']", List.class)
                .get(0);


        return new MarketVisibleSearchResults(
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
