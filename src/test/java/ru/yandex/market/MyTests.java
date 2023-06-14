package ru.yandex.market;

import com.browserup.harreader.model.Har;
import com.browserup.harreader.model.HarEntry;
import com.jayway.jsonpath.JsonPath;
import core.BaseTest;
import core.Helper;
import core.MarketVisibleSearchResults;
import core.WaitUtils;
import io.qameta.allure.Feature;
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
        yandexMarketMainPage.clickToCatalogButton();
        yandexMarketMainPage.hoverToCategory("Ноутбуки и компьютеры");
        yandexMarketMainPage.clickToSubcategory("Ноутбуки");

        YandexMarketSERPFactory yandexMarketSERPFactory = new YandexMarketSERPFactory(driver);
        yandexMarketSERPFactory.manufacturerFilter.clickShowAllButton();
        yandexMarketSERPFactory.manufacturerFilter.chooseManufacturer("Lenovo");
        yandexMarketSERPFactory.manufacturerFilter.chooseManufacturer("HUAWEI");
        yandexMarketSERPFactory.priceFilter.setMinPrice(10000);
        yandexMarketSERPFactory.priceFilter.setMaxPrice(900000);

        //yandexMarketSERPFactory.scroolToFooter();
        yandexMarketSERPFactory.scroolToPagerMoreButton();

        Thread.sleep(5000);

        WaitUtils.waitForState(
                () -> yandexMarketSERPFactory.getAllSnippets(),
                allSnippets -> allSnippets.size() > 0);

        Map<String, String> allSnippets = yandexMarketSERPFactory.map;

        WaitUtils.waitForState(
                () -> getMostRecentHarEntryForSearchRequest(proxy.getHar()),
                harEntry -> harEntry
                        .getResponse()
                        .getContent()
                        .getSize() > 0);

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
